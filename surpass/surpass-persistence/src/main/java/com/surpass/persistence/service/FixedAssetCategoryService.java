package com.surpass.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssetCategory;
import com.surpass.entity.fa.dto.FixedAssetCategoryChangeDto;
import com.surpass.entity.fa.dto.FixedAssetCategoryPageDto;

public interface FixedAssetCategoryService extends IService<FixedAssetCategory> {
    Message<Page<FixedAssetCategory>> pageList(FixedAssetCategoryPageDto dto);

    Message<String> save(FixedAssetCategoryChangeDto dto);

    Message<String> update(FixedAssetCategoryChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
