package com.surpass.entity.fa.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/15 17:16
 */

@Data
public class AssetDepreciationVo {
    private String assetId;
    private String assetName;
    private BigDecimal originalValue;
    private BigDecimal beginAccumulatedDepreciation;
    private BigDecimal currentYearDepreciation;
    private BigDecimal endAccumulatedDepreciation;
    private BigDecimal endNetValue;
    private String monthlyMapStr;
    private String assetCode;
    private String categoryCode;
    private String categoryName;

    public Map<String, BigDecimal> getMonthlyMap() {
        Map<String, BigDecimal> map = new HashMap<>();
        if (monthlyMapStr != null) {
            for (String entry : monthlyMapStr.split(",")) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    map.put(parts[0], new BigDecimal(parts[1]));
                }
            }
        }
        return map;
    }

}
