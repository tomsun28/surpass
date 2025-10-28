package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.fa.FixedAssetCategory;
import com.surpass.entity.fa.dto.FixedAssetCategoryPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface FixedAssetCategoryMapper extends BaseMapper<FixedAssetCategory> {
    Page<FixedAssetCategory> pageList(Page page, @Param("Dto") FixedAssetCategoryPageDto dto);
}
