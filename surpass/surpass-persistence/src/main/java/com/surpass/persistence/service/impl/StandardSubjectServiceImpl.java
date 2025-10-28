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
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.dto.SubjectChangeDto;
import com.surpass.entity.book.dto.SubjectPageDto;
import com.surpass.entity.book.dto.BookSubjectTreeDto;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.standard.StandardSubject;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.StandardMapper;
import com.surpass.persistence.mapper.StandardSubjectMapper;
import com.surpass.persistence.service.StandardSubjectService;
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

import java.util.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/19 16:04
 */

@Slf4j
@Service
public class StandardSubjectServiceImpl extends ServiceImpl<StandardSubjectMapper, StandardSubject> implements StandardSubjectService {

    @Autowired
    StandardSubjectMapper standardSubjectMapper;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    StandardMapper standardsMapper;

    @Override
    public Message<Page<StandardSubject>> pageList(SubjectPageDto dto) {
        return new Message<>(Message.SUCCESS, standardSubjectMapper.pageList(new Page<>(1, 1000), dto));
    }

    @Override
    public List<MapTree<String>> tree(BookSubjectTreeDto dto) {
        String standardId = dto.getStandardId();
        List<StandardSubject> subjects = new ArrayList<>();
        //查询准则下科目
        if (StringUtils.isNotBlank(standardId)) {
            List<StandardSubject> subjectList = super.list(Wrappers.<StandardSubject>lambdaQuery()
                    .eq(StandardSubject::getStandardId, standardId)
                    .eq(StandardSubject::getStatus, 1)
            );
            subjects = new ArrayList<>(subjectList);
            //copySubjectToStandard(subjects,"1");
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
            //extraMap.put("balance", temp.getBalance());
            extraMap.put("pinyinCode", temp.getPinyinCode());
            extraMap.put("pinyinDisplayCode", temp.getPinyinDisplayCode());
            extraMap.put("displayName", temp.getDisplayName());
            stringTreeNode.setExtra(extraMap);

            treeNode.add(stringTreeNode);
        });
        List<MapTree<String>> tree = TreeUtil.build(treeNode, null);

        if (ObjectUtils.isEmpty(tree)) {
            tree = new ArrayList<>();
        }
        return tree;
    }


    public Message<String> reorgDisplayName(BookSubjectTreeDto dto){
    	 String standardId = dto.getStandardId();
         //科目编码统一位4-2-2-4
         //查询准则下科目
         if (StringUtils.isNotBlank(standardId)) {
        	 reorgSubjectName(standardId);
         }

    	return new Message<>(Message.SUCCESS, "重组名称成功");
    }

    void reorgSubjectName(String standardId){
    	List<StandardSubject> subjectList = super.list(Wrappers.<StandardSubject>lambdaQuery()
                .eq(StandardSubject::getStandardId, standardId)
        );

    	Map<String,StandardSubject> subjectMap =new HashMap<>();
		for(StandardSubject subject : subjectList) {
			subjectMap.put(subject.getCode(), subject);
		}

		//1级
        for(StandardSubject subject : subjectList) {
       	 if(subject.getCode().length() == 4) {
       		 subject.setDisplayName(subject.getName());
       		 subject.setPinyinDisplayCode(subject.getPinyinCode());
       		 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       	 }
        }
        //2级
        for(StandardSubject subject : subjectList) {
       	 if(subject.getCode().length() == 6) {
       		 String parentCode = subject.getCode().substring(0,4);
       		 StandardSubject  parentSubject= subjectMap.get(parentCode);
       		 if(parentSubject != null) {
       			 subject.setParentId(parentSubject.getId());
       			 subject.setDisplayName(parentSubject.getDisplayName()+"_"+subject.getName());
       			 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       			 subject.setPinyinDisplayCode(parentSubject.getPinyinDisplayCode()+"_"+ subject.getPinyinCode());
       		 }else {
       			 subject.setDisplayName(subject.getName());
       			 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       			 subject.setPinyinDisplayCode(subject.getPinyinCode());
       		 }
       	 }
        }
        //3级
        for(StandardSubject subject : subjectList) {
       	 if(subject.getCode().length() == 8) {
       		 String parentCode = subject.getCode().substring(0,6);
       		 StandardSubject  parentSubject= subjectMap.get(parentCode);
       		 if(parentSubject != null) {
       			 subject.setParentId(parentSubject.getId());
       			 subject.setDisplayName(parentSubject.getDisplayName()+"_"+subject.getName());
       			 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       			 subject.setPinyinDisplayCode(parentSubject.getPinyinDisplayCode()+"_"+ subject.getPinyinCode());
       		 }else {
       			 subject.setDisplayName(subject.getName());
       			 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       			 subject.setPinyinDisplayCode(subject.getPinyinCode());
       		 }
       	 }
        }
        //4级
        for(StandardSubject subject : subjectList) {
       	 if(subject.getCode().length() == 12) {
       		 String parentCode = subject.getCode().substring(0,8);
       		 StandardSubject  parentSubject= subjectMap.get(parentCode);
       		 if(parentSubject != null) {
       			 subject.setParentId(parentSubject.getId());
       			 subject.setDisplayName(parentSubject.getDisplayName()+"_"+subject.getName());
       			 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       			 subject.setPinyinDisplayCode(parentSubject.getPinyinDisplayCode()+"_"+ subject.getPinyinCode());
       		 }else {
       			 subject.setDisplayName(subject.getName());
       			 subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
       			 subject.setPinyinDisplayCode(subject.getPinyinCode());
       		 }
       	 }
        }
        super.saveOrUpdateBatch(subjectList);
    }

    @Override
    @Transactional
    public Message<String> save(SubjectChangeDto dto) {
        //新增时检查是否有同名的科目
        checkDuplicateSubjects(dto, false);

        StandardSubject subject = new StandardSubject();
        BeanUtil.copyProperties(dto, subject);

        //若不填，自动生成助记码，名称拼音首字母
        if(StringUtils.isBlank(subject.getPinyinCode())) {
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

        //设置等级和现金类型
        if (Objects.nonNull(parentId)) {
            StandardSubject subjectParent = super.getById(parentId);
            if(subjectParent != null) {
            	subject.setDisplayName(subjectParent.getDisplayName()+"_"+subject.getName());
            	subject.setPinyinDisplayCode(subjectParent.getPinyinDisplayCode()+"_"+subject.getPinyinCode());
                subject.setLevel(subjectParent.getLevel() + 1);
                if (subjectParent.getIsCash() == 1) {
                    subject.setIsCash(1);
                }
            }
            subject.setLevel(1);
        } else {
            subject.setLevel(1);
        }

        boolean save = super.save(subject);

        return save ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(SubjectChangeDto dto) {
        //修改时检查是否有同名的方法，不包括当前科目
        checkDuplicateSubjects(dto, true);

        String parentId = dto.getParentId();
        String currentId = dto.getId();
        String standardId = dto.getStandardId();

        //更改status检查
        Integer status = dto.getStatus();
        if (status == 0) {
            //禁用
            ifAbleDisable(standardId,currentId);
        } else {
            //启用
            isForbiddenParentSubject(currentId);
        }

        //检查是否为合法的移动操作
        //查询所有的子级别科目包括自己
        List<StandardSubject> subjectsSon = standardSubjectMapper.selectList(Wrappers.<StandardSubject>lambdaQuery()
                .eq(StandardSubject::getStandardId, standardId)
                .eq(StandardSubject::getStandardId, standardId)
                .like(StandardSubject::getIdPath, currentId));
        if (subjectsSon.stream().anyMatch(subject -> Objects.equals(parentId, subject.getId()))) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getCode(),
                    BookBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getMsg()
            );
        }

        StandardSubject subject = new StandardSubject();
        BeanUtil.copyProperties(dto, subject);

        //若不填，自动生成助记码，名称拼音首字母
        if(StringUtils.isBlank(subject.getPinyinCode())) {
        	subject.setPinyinCode(StrUtils.getPinYinShort(subject.getName()));
        }

        //辅助核算
        if (Objects.isNull(subject.getAuxiliary())) {
            subject.setAuxiliary("[]");
        }

        if (Objects.isNull(parentId) || StringUtils.isBlank(parentId)) {
            //设置f父级为空
            subject.setParentId(null);
        }

        LambdaUpdateWrapper<StandardSubject> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StandardSubject::getAuxiliary, subject.getAuxiliary());
        updateWrapper.set(StandardSubject::getParentId, parentId);
        updateWrapper.eq(StandardSubject::getId, currentId);

        //设置ID路径
        subject.setIdPath(generateIdPath(parentId, currentId));

        if (Objects.isNull(parentId) || StringUtils.isEmpty(parentId)) {
            //设置等级
            subject.setLevel(1);
        } else {
            StandardSubject subjectParent = super.getById(parentId);
            subject.setLevel(subjectParent.getLevel() + 1);
            if(subjectParent != null) {
            	subject.setDisplayName(subjectParent.getDisplayName()+"_"+subject.getName());
            	subject.setPinyinDisplayCode(subjectParent.getPinyinDisplayCode()+"_"+subject.getPinyinCode());
            }
        }

        boolean update = standardSubjectMapper.update(subject, updateWrapper) > 0;

        //更新所有的子级科目
        List<StandardSubject> updatedSubjects = subjectsSon.stream()
                .filter(temp -> !temp.getId().equals(currentId))
                .map(temp -> {
                    String subParentId = temp.getParentId();
                    String subId = temp.getId();
                    temp.setIsCash(subject.getIsCash());
                    temp.setIdPath(generateIdPath(subParentId, subId));
                    return temp;
                })
                .toList();
        super.updateBatchById(updatedSubjects);

        return update ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();
        LambdaUpdateWrapper<StandardSubject> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(StandardSubject::getId, listIds);
        List<StandardSubject> delList  = super.list(wrapper);
        for(StandardSubject s : delList) {
	        //判断状态
	        isDisable(s.getStandardId(),listIds);

	        //是否包含下级科目
	        isExistSubSubject(s.getStandardId(),listIds);
        }

        boolean result = super.removeByIds(listIds);
        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    /**
     * @Description: 检查是否存在重复的名称或编码
     * @Param: [dto, isUpdate] 如果是更新操作需要传递 `true`
     * @return: void
     * @Date: 2024/12/23 17:43
     */
    public void checkDuplicateSubjects(SubjectChangeDto dto, boolean isUpdate) {
        String code = dto.getCode();
        String name = dto.getName();
        String standardId = dto.getStandardId();

        LambdaQueryWrapper<StandardSubject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StandardSubject::getStandardId, standardId);
        if (isUpdate) {
            // 如果是更新操作，排除当前 ID
            queryWrapper.ne(StandardSubject::getId, dto.getId());
        }
        //仅检测科目编码
        queryWrapper.and(wrapper ->wrapper.eq(StandardSubject::getCode, code));

        List<StandardSubject> duplicates = super.list(queryWrapper);
        if (ObjectUtils.isNotEmpty(duplicates)) {
            for (StandardSubject duplicate : duplicates) {
                if (duplicate.getName().equals(name)) {
                    throw new BusinessException(
                            BookBusinessExceptionEnum.DUPLICATE_SUBJECTS_EXIST.getCode(),
                            BookBusinessExceptionEnum.DUPLICATE_SUBJECTS_EXIST.getMsg()
                    );
                }
                if (duplicate.getCode().equals(code)) {
                    throw new BusinessException(
                            BookBusinessExceptionEnum.DUPLICATE_SUBJECTSCODE_EXIST.getCode(),
                            BookBusinessExceptionEnum.DUPLICATE_SUBJECTSCODE_EXIST.getMsg()
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

        StandardSubject parent = super.getById(parentId);
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

    /**
     * @Description: 是否包含子级科目
     * @Param: [ids]
     * @return: void
     * @Author: xZen
     * @Date: 2024/12/26 14:44
     */
    private void isExistSubSubject(String standardId,List<String> ids) {
        LambdaQueryWrapper<StandardSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StandardSubject::getParentId, ids)
        	.eq(StandardSubject::getStandardId, standardId);
        List<StandardSubject> subjects = standardSubjectMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.SUB_SUBJECTS_EXISTS.getCode(),
                    BookBusinessExceptionEnum.SUB_SUBJECTS_EXISTS.getMsg());
        }
    }

    /**
     * @Description: 判断集合是否为活跃状态
     * @Param: [ids]
     * @return: void
     * @Author: xZen
     * @Date: 2025/1/3 10:24
     */
    private void isDisable(String standardId,List<String> ids) {
        LambdaQueryWrapper<StandardSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StandardSubject::getStatus, 1);
        wrapper.in(StandardSubject::getId, ids)
        		.eq(StandardSubject::getStandardId, standardId);

        List<StandardSubject> subjects = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg());
        }
    }

    /**
     * @Description: 禁用科目时检查是否符合条件
     * @Param: [id]
     * @return: void
     * @Author: xZen
     * @Date: 2024/12/26 14:55
     */
    private void ifAbleDisable(String standardId,String id) {
        //检查是否有启用的子级科目
        LambdaQueryWrapper<StandardSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StandardSubject::getParentId, id)
        		.eq(StandardSubject::getStatus, 1)
        		.eq(StandardSubject::getStandardId, standardId);
        List<StandardSubject> subjects = standardSubjectMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(subjects)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.SUB_SUBJECTS_ACTIVE.getCode(),
                    BookBusinessExceptionEnum.SUB_SUBJECTS_ACTIVE.getMsg());
        }
    }

    /**
     * @Description: 查询父级科目是否被禁用
     * @Param: [id]
     * @return: void
     * @Author: xZen
     * @Date: 2024/12/26 16:17
     */
    private void isForbiddenParentSubject(String id) {
        String parentId = super.getById(id).getParentId();
        if (StringUtils.isNotBlank(parentId)) {
            StandardSubject parent = super.getById(parentId);
            if (Objects.nonNull(parent) && 0 == parent.getStatus()) {
                throw new BusinessException(
                        BookBusinessExceptionEnum.PARENT_ORGS_FORBIDDEN.getCode(),
                        BookBusinessExceptionEnum.PARENT_ORGS_FORBIDDEN.getMsg());
            }
        }
    }

    public void copySubjectToStandard(List<StandardSubject> subjects,String standardId) {
    	List<StandardSubject> subjectList = super.list(Wrappers.<StandardSubject>lambdaQuery()
                .eq(StandardSubject::getStandardId, standardId)
                .eq(StandardSubject::getStatus, 1)
        );
    	if(CollectionUtils.isEmpty(subjectList)) {
    		for(StandardSubject s : subjects) {
    			s.setId(null);
    			s.setStandardId(standardId);
    		}
    		standardSubjectMapper.insert(subjects);
    	}
    }
}
