package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssetCategory;
import com.surpass.entity.fa.dto.FixedAssetCategoryChangeDto;
import com.surpass.entity.fa.dto.FixedAssetCategoryPageDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.FixedAssetCategoryMapper;
import com.surpass.persistence.service.FixedAssetCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/1 9:49
 */

@Service
@RequiredArgsConstructor
public class FixedAssetCategoryServiceImpl extends ServiceImpl<FixedAssetCategoryMapper, FixedAssetCategory> implements FixedAssetCategoryService {

    private final FixedAssetCategoryMapper fixedAssetCategoryMapper;

    @Override
    public Message<Page<FixedAssetCategory>> pageList(FixedAssetCategoryPageDto dto) {
        Page<FixedAssetCategory> fixedAssetCategoryPage = fixedAssetCategoryMapper.pageList(dto.build(), dto);

        return Message.ok(fixedAssetCategoryPage);
    }

    @Override
    @Transactional
    public Message<String> save(FixedAssetCategoryChangeDto dto) {
        checkNameAndCode(dto, false);

        FixedAssetCategory fixedAssetCategory = BeanUtil.copyProperties(dto, FixedAssetCategory.class);

        boolean result = super.save(fixedAssetCategory);

        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(FixedAssetCategoryChangeDto dto) {
        checkNameAndCode(dto, true);

        FixedAssetCategory fixedAssetCategory = BeanUtil.copyProperties(dto, FixedAssetCategory.class);

        boolean result = super.updateById(fixedAssetCategory);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    private void checkNameAndCode(FixedAssetCategoryChangeDto dto, boolean isEdit) {
        String id = dto.getId();
        String name = dto.getCategoryName();
        String code = dto.getCategoryCode();
        String bookId = dto.getBookId();

        // 校验名称是否重复
        boolean nameExists = super.lambdaQuery()
                .eq(FixedAssetCategory::getBookId, bookId)
                .eq(FixedAssetCategory::getCategoryName, name)
                .ne(isEdit, FixedAssetCategory::getId, id)
                .exists();

        // 校验编码是否重复
        boolean codeExists = super.lambdaQuery()
                .eq(FixedAssetCategory::getBookId, bookId)
                .eq(FixedAssetCategory::getCategoryCode, code)
                .ne(isEdit, FixedAssetCategory::getId, id)
                .exists();

        if (nameExists && codeExists) {
            throw new BusinessException(50001, "分类名称和分类编码均已存在");
        } else if (nameExists) {
            throw new BusinessException(50001, "分类名称已存在");
        } else if (codeExists) {
            throw new BusinessException(50001, "分类编码已存在");
        }
    }
}
