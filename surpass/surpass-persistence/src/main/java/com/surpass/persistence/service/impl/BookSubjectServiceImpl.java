/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.surpass.enums.OnOffEnum;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.surpass.entity.BaseSubject;
import com.surpass.entity.Message;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.book.dto.BookChangeDto;
import com.surpass.entity.book.dto.SubjectChangeDto;
import com.surpass.entity.book.dto.SubjectPageDto;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.standard.StandardSubject;
import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.BookSubjectMapper;
import com.surpass.persistence.mapper.StandardSubjectMapper;
import com.surpass.persistence.service.BookSubjectService;
import com.surpass.persistence.service.StatementSubjectBalanceService;
import com.surpass.util.StrUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.tree.MapTree;
import org.dromara.hutool.core.tree.TreeNode;
import org.dromara.hutool.core.tree.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/1/16 16:13
 */

@Slf4j
@Service
public class BookSubjectServiceImpl extends ServiceImpl<BookSubjectMapper, BookSubject> implements BookSubjectService {

    @Autowired
    BookSubjectMapper bookSubjectMapper;

    @Autowired
    StandardSubjectMapper standardSubjectMapper;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    private StatementSubjectBalanceService subjectBalanceService;

    @Override
    public BookSubject getById(String bookId, String id) {
        LambdaQueryWrapper<BookSubject> lqw = Wrappers.lambdaQuery();
        lqw.eq(BookSubject::getBookId, bookId);
        lqw.eq(BookSubject::getId, id);
        BookSubject bookSubject = bookSubjectMapper.selectOne(lqw);

        boolean hasVoucher = subjectBalanceService.hasVoucher(bookId, Collections.singletonList(id));
        bookSubject.setHasVoucher(hasVoucher);
        return bookSubject;
    }


    @Override
    public Message<Page<BookSubject>> pageList(SubjectPageDto dto) {
        return new Message<>(Message.SUCCESS, bookSubjectMapper.pageListByBook(dto.build(), dto));
    }

    @Override
    @Transactional
    public Message<String> save(SubjectChangeDto dto) {
        //新增时检查是否有同名的科目
        checkDuplicateSubjects(dto, false);

        BookSubject subject = new BookSubject();
        BeanUtil.copyProperties(dto, subject);

        //若不填，自动生成助记码，名称拼音首字母
        if (StringUtils.isBlank(subject.getPinyinCode())) {
            subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
        }

        //手动添加
        subject.setSystemDefault(2);

        String parentId = dto.getParentId();

        //生成ID
        String currentId = identifierGenerator.nextId(subject).toString();
        subject.setId(currentId);

        //设置id路径
        String idPath = generateIdPath(parentId, currentId);
        subject.setIdPath(idPath);

        //设置等级
        if (Objects.nonNull(parentId)) {
            BookSubject subjectParent = super.getById(parentId);
            subject.setLevel(subjectParent.getLevel() + 1);
            subject.setDisplayName(subjectParent.getDisplayName() + "_" + subject.getName());
            subject.setPinyinDisplayCode(subjectParent.getPinyinDisplayCode() + "_" + subject.getPinyinCode());
            //判断是否符合辅助核算层级
            ifParentExistAuxiliary(parentId);
            //设置现金类科目
            if (subjectParent.getIsCash() == 1) {
                subject.setIsCash(1);
            }
        } else {
            subject.setLevel(1);
        }

        boolean save = super.save(subject);

        // 同步到科目余额表，余额初始化为0
        subjectBalanceService.save(subject);

        return save ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(SubjectChangeDto dto) {
        //修改时检查是否有同名的方法，不包括当前科目
        checkDuplicateSubjects(dto, true);

        String parentId = dto.getParentId();
        String currentId = dto.getId();
        String bookId = dto.getBookId();

        //更改status检查
        Integer status = dto.getStatus();
        if (status == 0) {
            //禁用
            ifAbleDisable(currentId);
        } else {
            //启用
            isForbiddenParentSubject(currentId);
        }

        //检查是否为合法的移动操作
        //查询所有的子级别科目包括自己
        List<BookSubject> subjectsSon = bookSubjectMapper.selectList(Wrappers.<BookSubject>lambdaQuery()
                .eq(BookSubject::getBookId, bookId)
                .like(BookSubject::getIdPath, currentId));
        if (subjectsSon.stream().anyMatch(subject -> Objects.equals(parentId, subject.getId()))) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getCode(),
                    BookBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getMsg()
            );
        }

        BookSubject subject = new BookSubject();
        BeanUtil.copyProperties(dto, subject);

        //若不填，自动生成助记码，名称拼音首字母
        if (StringUtils.isBlank(subject.getPinyinCode())) {
            subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
        }

        //辅助核算
        if (StringUtils.isNotEmpty(subject.getAuxiliary()) && !"[]".equals(subject.getAuxiliary())) {
            //判断是否符合辅助核算层级
            checkWhetherCompliesAuxiliary(currentId);
        }

        if (Objects.isNull(parentId) || StringUtils.isBlank(parentId)) {
            //设置父级为空
            subject.setParentId(null);
        } else {
            //判断是否符合辅助核算层级
            ifParentExistAuxiliary(parentId);
        }

        // 判断是否有凭证,有凭证时不能随意修改
        boolean hasVoucher = subjectBalanceService.hasVoucher(bookId, Collections.singletonList(currentId));
        if (hasVoucher) {
            subject.setDirection(null);
            subject.setAuxiliary(null);
            subject.setIsCash(null);
        }

        LambdaUpdateWrapper<BookSubject> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(BookSubject::getAuxiliary, subject.getAuxiliary());
        updateWrapper.set(BookSubject::getParentId, parentId);
        updateWrapper.eq(BookSubject::getId, currentId);

        //设置ID路径
        subject.setIdPath(generateIdPath(parentId, currentId));

        if (Objects.isNull(parentId) || StringUtils.isEmpty(parentId)) {
            //设置等级
            subject.setLevel(1);
        } else {
            BookSubject subjectParent = super.getById(parentId);
            subject.setLevel(subjectParent.getLevel() + 1);
            subject.setDisplayName(subjectParent.getDisplayName() + "_" + subject.getName());
            subject.setPinyinDisplayCode(subjectParent.getPinyinDisplayCode() + "_" + subject.getPinyinCode());
        }

        //更新科目
        boolean update = super.update(subject, updateWrapper);
//        insertAuxiliaryData(isAuxiliary, currentId, subject);

        //更新所有的子级科目
        List<BookSubject> updatedSubjects = subjectsSon.stream()
                .filter(temp -> !temp.getId().equals(currentId))
                .peek(temp -> {
                    String subParentId = temp.getParentId();
                    String subId = temp.getId();
                    temp.setIdPath(generateIdPath(subParentId, subId));
                    temp.setIsCash(subject.getIsCash());
                })
                .toList();
        super.updateBatchById(updatedSubjects);

        // 同步到科目余额表
        subjectBalanceService.update(subject);

        return update ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    private void ifAbleDisable(String id) {
        //检查是否有启用的子级科目
        LambdaQueryWrapper<BookSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookSubject::getParentId, id).eq(BookSubject::getStatus, 1);
        List<BookSubject> subjects = bookSubjectMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.SUB_SUBJECTS_ACTIVE.getCode(),
                    "请先禁用当前账套下的活跃子科目"
            );
        }
    }

    private void isForbiddenParentSubject(String id) {
        String parentId = super.getById(id).getParentId();
        if (StringUtils.isNotBlank(parentId)) {
            BookSubject parent = super.getById(parentId);
            if (Objects.nonNull(parent) && 0 == parent.getStatus()) {
                throw new BusinessException(
                        BookBusinessExceptionEnum.PARENT_ORGS_FORBIDDEN.getCode(),
                        BookBusinessExceptionEnum.PARENT_ORGS_FORBIDDEN.getMsg());
            }
        }
    }

    public void checkDuplicateSubjects(SubjectChangeDto dto, boolean isUpdate) {
        String code = dto.getCode();
        String name = dto.getName();
        String bookId = dto.getBookId();

        LambdaQueryWrapper<BookSubject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookSubject::getBookId, bookId);
        if (isUpdate) {
            // 如果是更新操作，排除当前 ID
            queryWrapper.ne(BookSubject::getId, dto.getId());
        }
        //科目编码不能有重复
        queryWrapper.and(wrapper -> wrapper.eq(BookSubject::getCode, code));

        List<BookSubject> duplicates = super.list(queryWrapper);
        if (ObjectUtils.isNotEmpty(duplicates)) {
            for (BookSubject duplicate : duplicates) {
                if (duplicate.getName().equals(name)) {
                    throw new BusinessException(
                            BookBusinessExceptionEnum.DUPLICATE_SUBJECTS_EXIST.getCode(),
                            "当前账套下已存在相同的科目名称，请重新输入"
                    );
                }
                if (duplicate.getCode().equals(code)) {
                    throw new BusinessException(
                            BookBusinessExceptionEnum.DUPLICATE_SUBJECTSCODE_EXIST.getCode(),
                            "当前账套下已存在相同的科目编码，请重新输入"
                    );
                }
            }
        }
    }

    /**
     * @Description: 生成所属的ID路径
     * @Param: [parentId, currentId]
     * @return: java.lang.String
     * @Author: xZen
     * @Date: 2024/12/23 17:44
     */
    private String generateIdPath(String parentId, String currentId) {
        if (StringUtils.isNotBlank(parentId) && !"0".equals(parentId)) {
            String parentPath = generateIdPathRecursive(parentId, 0);
            if (StringUtils.isBlank(parentPath)) {
                return "/" + currentId;
            } else {
                return "/" + parentPath + "/" + currentId;
            }
        }
        return "/" + currentId;
    }

    /**
     * @Description: 递归生成ID路径
     * @Param: [parentId, depth]
     * @return: java.lang.String
     * @Author: xZen
     * @Date: 2024/12/23 17:45
     */
    private String generateIdPathRecursive(String parentId, int depth) {
        if (depth > 9) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DUPLICATE_DEEP_LIMIT.getCode(),
                    BookBusinessExceptionEnum.DUPLICATE_DEEP_LIMIT.getMsg()
            );
        }

        if (StringUtils.isBlank(parentId) || "0".equals(parentId)) {
            return "";
        }

        BookSubject parent = super.getById(parentId);
        if (Objects.isNull(parent)) {
            return "";
        }

        String parentPath = generateIdPathRecursive(parent.getParentId(), depth + 1);
        if (parentPath.isBlank()) {
            return parentId;
        } else {
            return parentPath + "/" + parentId;
        }
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();

        //判断状态
        isDisable(listIds);

        //是否包含下级科目
        isExistSubSubject(listIds);

        // 科目已有凭证，不能删除。
        boolean hasVoucher = subjectBalanceService.hasVoucher(dto.getBookId(), listIds);
        if (hasVoucher) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DELETE_HAS_VOUCHER.getCode(),
                    BookBusinessExceptionEnum.DELETE_HAS_VOUCHER.getMsg());
        }

        LambdaQueryWrapper<BookSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BookSubject::getId, listIds);
        wrapper.or();
        wrapper.in(BookSubject::getBelongSubjectId, listIds);
        boolean result = super.remove(wrapper);

        // 同步到科目余额表
        subjectBalanceService.delete(listIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    private void isDisable(List<String> ids) {
        LambdaQueryWrapper<BookSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookSubject::getStatus, 1);
        wrapper.in(BookSubject::getId, ids);

        List<BookSubject> subjects = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg());
        }
    }

    private void isExistSubSubject(List<String> ids) {
        LambdaQueryWrapper<BookSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BookSubject::getParentId, ids);
        List<BookSubject> subjects = bookSubjectMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.SUB_SUBJECTS_EXISTS.getCode(),
                    BookBusinessExceptionEnum.SUB_SUBJECTS_EXISTS.getMsg());
        }
    }

    /**
     * @Description: 修改科目时校验是否可以设置辅助核算
     * @Param: [currentId]
     * @return: void
     * @Author: xZen
     * @Date: 2025/3/12 15:58
     */
    private void checkWhetherCompliesAuxiliary(String currentId) {
        LambdaQueryWrapper<BookSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookSubject::getParentId, currentId);
        List<BookSubject> subjects = bookSubjectMapper.selectList(wrapper);

        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(500001, "当前层级科目含有子科目，无法设置辅助核算");
        }
    }

    /**
     * @Description: 判断父级科目是否已设置辅助核算
     * @return: void
     * @Author: xZen
     * @Date: 2025/3/12 16:15
     */
    private void ifParentExistAuxiliary(String parentId) {
        // 创建查询条件
        LambdaQueryWrapper<BookSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookSubject::getId, parentId)
                .and(qw -> qw.isNotNull(BookSubject::getAuxiliary)
                        .ne(BookSubject::getAuxiliary, "[]")
                        .ne(BookSubject::getAuxiliary, ""));


        // 直接使用count查询，无需获取完整对象列表
        long count = super.count(wrapper);
        if (count > 0) {
            throw new BusinessException(500001, "所选父级科目已设置辅助核算，无法选择该父级科目");
        }
    }

    /**
     * @Description: 设置辅助核算科目
     * @Param: [bookSubject]
     * @return: void
     * @Author: xZen
     * @Date: 2025/3/12 16:55
     */
    private void setAuxiliarySubject(BookSubject bookSubject) {
        bookSubject.setIsAuxiliary(1);
        String auxiliary = bookSubject.getAuxiliary();
        String code = bookSubject.getCode();
        String name = bookSubject.getName();

        try {
            List<Map<String, Object>> auxiliaryList = new ObjectMapper().readValue(
                    auxiliary,
                    new TypeReference<>() {
                    }
            );

            // Use StringBuilder for efficient string concatenation
            StringBuilder newCodeBuilder = new StringBuilder(code);
            StringBuilder newNameBuilder = new StringBuilder(name);

            // Iterate through the auxiliary list to build the new code and name
            for (Map<String, Object> item : auxiliaryList) {
                newCodeBuilder.append("_0").append(item.get("value"));
                newNameBuilder.append("_").append(item.get("label"));
            }

            // Set the new values
            bookSubject.setCode(newCodeBuilder.toString());
            bookSubject.setName(newNameBuilder.toString());

        } catch (Exception e) {
            log.error("解析辅助核算数据失败", e);
        }
    }

    @Override
    public List<MapTree<String>> tree(String bookId) {
        List<BaseSubject> subjects = new ArrayList<>();
        //查询账簿下科目
        if (StringUtils.isNotBlank(bookId)) {
            List<BookSubject> setSubjects = bookSubjectMapper.selectList(Wrappers.<BookSubject>lambdaQuery()
                    .eq(BookSubject::getBookId, bookId)
                    .eq(BookSubject::getStatus, OnOffEnum.ON.getCode())
            );
            subjects = new ArrayList<>(setSubjects);
        }

        List<StatementSubjectBalance> listSubjectBalance=subjectBalanceService.selectSubjectBalance(bookId, null);
        HashMap<String,StatementSubjectBalance> subjectBalanceMap = new HashMap<>();
        for(StatementSubjectBalance sb : listSubjectBalance) {
        	subjectBalanceMap.put(sb.getSubjectCode(), sb);
        }

        List<TreeNode<String>> treeNode = new ArrayList<>();
        subjects.forEach(temp -> {
            TreeNode<String> stringTreeNode = new TreeNode<>(
                    temp.getId(),
                    temp.getParentId(),
                    String.format("%s-%s", temp.getCode(), temp.getName()),
                    temp.getCode()
            );

            // 为每个节点创建独立的 extraMap
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("idPath", temp.getIdPath());
            extraMap.put("direction", temp.getDirection());
            extraMap.put("code", temp.getCode());
            extraMap.put("auxiliary", temp.getAuxiliary());
            subjectBalanceMap.get(temp.getCode());
            StatementSubjectBalance sb = subjectBalanceMap.get(temp.getCode());
            extraMap.put("balance", (sb != null ) ? sb.getBalance():BigDecimal.ZERO);
            //extraMap.put("balance", temp.getBalance());
            extraMap.put("pinyinCode", temp.getPinyinCode());
            extraMap.put("pinyinDisplayCode", temp.getPinyinDisplayCode());
            extraMap.put("displayName", temp.getDisplayName());
            extraMap.put("isCash", temp.getIsCash());
            stringTreeNode.setExtra(extraMap);

            treeNode.add(stringTreeNode);
        });

        List<MapTree<String>> tree = TreeUtil.build(treeNode, null);

        if (ObjectUtils.isEmpty(tree)) {
            tree = new ArrayList<>();
        }
        return tree;
    }

    @Override
    public Message<String> reorgDisplayName(String bookId) {
        //科目编码统一位4-2-2-4
        List<BookSubject> subjectList = bookSubjectMapper.selectList(Wrappers.<BookSubject>lambdaQuery()
                .eq(BookSubject::getBookId, bookId)
                .eq(BookSubject::getStatus, 1)
                .and(q -> q.ne(BookSubject::getIsAuxiliary, 1).or().isNull(BookSubject::getIsAuxiliary))
        );

        Map<String, BookSubject> subjectMap = new HashMap<>();
        for (BookSubject subject : subjectList) {
            subjectMap.put(subject.getCode(), subject);
        }
        //1级
        for (BookSubject subject : subjectList) {
            if (subject.getCode().length() == 4) {
            	subject.setIdPath("/"+subject.getId());
                subject.setDisplayName(subject.getName());
                subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                subject.setPinyinDisplayCode(subject.getPinyinCode());
            }
        }
        //2级
        for (BookSubject subject : subjectList) {
            if (subject.getCode().length() == 6) {
                String parentCode = subject.getCode().substring(0, 4);
                BookSubject parentSubject = subjectMap.get(parentCode);
                if (parentSubject != null) {
                    subject.setParentId(parentSubject.getId());
                    subject.setIdPath(parentSubject.getIdPath()+"/"+subject.getId());
                    subject.setDisplayName(parentSubject.getDisplayName() + "_" + subject.getName());
                    subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                    subject.setPinyinDisplayCode(parentSubject.getPinyinDisplayCode() + "_" + subject.getPinyinCode());
                } else {
                    subject.setDisplayName(subject.getName());
                    subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                    subject.setPinyinDisplayCode(subject.getPinyinCode());
                }
            }
        }
        //3级
        for (BookSubject subject : subjectList) {
            if (subject.getCode().length() == 8) {
                String parentCode = subject.getCode().substring(0, 6);
                BookSubject parentSubject = subjectMap.get(parentCode);
                if (parentSubject != null) {
                    subject.setParentId(parentSubject.getId());
                    subject.setIdPath(parentSubject.getIdPath()+"/"+subject.getId());
                    subject.setDisplayName(parentSubject.getDisplayName() + "_" + subject.getName());
                    subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                    subject.setPinyinDisplayCode(parentSubject.getPinyinDisplayCode() + "_" + subject.getPinyinCode());
                } else {
                    subject.setDisplayName(subject.getName());
                    subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                    subject.setPinyinDisplayCode(subject.getPinyinCode());
                }
            }
        }
        //4级
        for (BookSubject subject : subjectList) {
            if (subject.getCode().length() == 12) {
                String parentCode = subject.getCode().substring(0, 8);
                BookSubject parentSubject = subjectMap.get(parentCode);
                if (parentSubject != null) {
                    subject.setParentId(parentSubject.getId());
                    subject.setIdPath(parentSubject.getIdPath()+"/"+subject.getId());
                    subject.setDisplayName(parentSubject.getDisplayName() + "_" + subject.getName());
                    subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                    subject.setPinyinDisplayCode(parentSubject.getPinyinDisplayCode() + "_" + subject.getPinyinCode());
                } else {
                    subject.setDisplayName(subject.getName());
                    subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
                    subject.setPinyinDisplayCode(subject.getPinyinCode());
                }
            }
        }
        bookSubjectMapper.insertOrUpdate(subjectList);
        return null;
    }

    /**
     * Select Subject by bookId and subjectCode
     *
     * @param bookId
     * @param subjectCode
     * @return
     */
    @Override
    public BookSubject selectSubject(String bookId, String subjectCode) {
        LambdaQueryWrapper<BookSubject> bookLqw = Wrappers.lambdaQuery();
        bookLqw.eq(BookSubject::getCode, subjectCode);
        bookLqw.eq(BookSubject::getBookId, bookId);
        bookLqw.eq(BookSubject::getDeleted, "n");
        BookSubject bookSubject = bookSubjectMapper.selectOne(bookLqw);
        List<String>subjectCodes = new ArrayList<>();
        subjectCodes.add(subjectCode);
        List<StatementSubjectBalance> listSubjectBalance = subjectBalanceService.selectSubjectBalance(bookId, subjectCodes);
        if(CollectionUtils.isNotEmpty(listSubjectBalance)) {
        	bookSubject.setBalance(listSubjectBalance.get(0).getBalance());
        }else {
        	bookSubject.setBalance(BigDecimal.ZERO);
        }
        return bookSubject;
    }

    /**
     * Select Subject and Child by bookId and subjectCode
     *
     * @param bookId
     * @param subjectCode
     * @return
     */
    @Override
    public List<BookSubject> selectSubjectAndChild(String bookId, String subjectCode) {
        LambdaQueryWrapper<BookSubject> bookLqw = Wrappers.lambdaQuery();
        bookLqw.likeRight(BookSubject::getCode, subjectCode);
        bookLqw.eq(BookSubject::getBookId, bookId);
        bookLqw.eq(BookSubject::getDeleted, "n");
        List<BookSubject> listSubject = bookSubjectMapper.selectList(bookLqw);

        List<String>subjectCodes = new ArrayList<>();
        for(BookSubject s : listSubject) {
        	subjectCodes.add(s.getCode());
        }

        List<StatementSubjectBalance> listSubjectBalance=subjectBalanceService.selectSubjectBalance(bookId, subjectCodes);
        HashMap<String,StatementSubjectBalance> subjectBalanceMap = new HashMap<>();
        for(StatementSubjectBalance sb : listSubjectBalance) {
        	subjectBalanceMap.put(sb.getSubjectCode(), sb);
        }

        for(BookSubject s : listSubject) {
        	StatementSubjectBalance sb = subjectBalanceMap.get(s.getCode());
        	if(sb != null) {
            	s.setBalance(sb.getBalance());
            }else {
            	s.setBalance(BigDecimal.ZERO);
            }
        }
        return listSubject;
    }

	@Override
	public boolean deleteByBookIds(List<String> bookIds) {
		LambdaQueryWrapper<BookSubject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.in(BookSubject::getBookId, bookIds);
        bookSubjectMapper.delete(subjectLambdaQueryWrapper);
		return true;
	}


	/**
	 * 插入关联账套和科目
	 */
	@Override
	public boolean initBookSubject(BookChangeDto dto) {
		 String standardId = dto.getStandardId();
        List<StandardSubject> standardSubjects =
        		standardSubjectMapper.selectList(
        				Wrappers.<StandardSubject>lambdaQuery().eq(StandardSubject::getStandardId, standardId));

        if (ObjectUtils.isNotEmpty(standardSubjects)) {

            // 创建新数据集合（避免直接修改原始集合）
            List<BookSubject> newSubjects = standardSubjects.stream()
                    .map(original -> {
                        BookSubject setSubject = new BookSubject();
                        setSubject.setBookId(dto.getId());
                        BeanUtil.copyProperties(original, setSubject,
                                "id","deleted", "createdBy", "createdDate", "modifiedBy", "modifiedDate");
                        setSubject.setOriginalId(original.getId());
                        return setSubject;
                    })
                    .toList();

            // 批量插入新数据（确保 Mapper 支持批量插入）
            bookSubjectMapper.insertBatch(newSubjects);

            // 构建 ID 映射关系：原始 ID -> 新 ID
            // `getOriginalId` 返回旧的 ID
            Map<String, String> idMapping = newSubjects.stream()
                    .collect(Collectors.toMap(BookSubject::getOriginalId, BookSubject::getId));

            // 更新新数据集合中的 parent_id
            newSubjects.forEach(subject -> {
                String newParentId = null;
                if (subject.getParentId() != null) {
                    newParentId = idMapping.get(subject.getParentId());
                    subject.setParentId(newParentId);
                }
            });

            bookSubjectMapper.updateById(newSubjects);

            newSubjects.forEach(subject -> subject.setIdPath(generateIdPath(subject.getParentId(), subject.getId())));

            bookSubjectMapper.updateById(newSubjects);
        }
		return true;
	}

}
