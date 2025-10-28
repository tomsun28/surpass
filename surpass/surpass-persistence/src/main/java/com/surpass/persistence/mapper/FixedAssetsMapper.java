package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.FixedAssetsPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FixedAssetsMapper extends BaseMapper<FixedAssets> {
    Page<FixedAssets> pageList(Page page, @Param("Dto") FixedAssetsPageDto dto);
}
