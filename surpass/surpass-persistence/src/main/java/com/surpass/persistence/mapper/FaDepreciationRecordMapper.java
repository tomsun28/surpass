package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.fa.FaDepreciationRecord;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.DepreciationRecordPageDto;
import com.surpass.entity.fa.vo.AssetDepreciationVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface FaDepreciationRecordMapper extends BaseMapper<FaDepreciationRecord> {
    List<FixedAssets> getFixedAssetsList(FixedAssets fixedAssets);

    @MapKey("assetId")
    Map<String, BigDecimal> getAccumulatedMap(@Param("assetIds") List<String> assetIds, @Param("bookId") String bookId);

    Page<AssetDepreciationVo> pageList(Page page, @Param("dto") DepreciationRecordPageDto faDepreciationRecordPageDto);
}
