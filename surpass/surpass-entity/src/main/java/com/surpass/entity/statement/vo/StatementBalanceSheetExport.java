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
 

package com.surpass.entity.statement.vo;

import com.surpass.util.excel.ExcelExportCfg;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 资产负债表导出模板数据
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementBalanceSheetExport implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8096547651691024294L;
	private String companyName;
    private String date;
    private List<AssetLiability> assetLiabilityList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetLiability {
        private String assetItemName;
        private Integer assetRowNum;

        @ExcelExportCfg
        private BigDecimal assetCurrentBalance;
        @ExcelExportCfg
        private BigDecimal assetInitialBalance;

        private String liabilityItemName;
        private Integer liabilityRowNum;
        @ExcelExportCfg
        private BigDecimal liabilityCurrentBalance;
        @ExcelExportCfg
        private BigDecimal liabilityInitialBalance;
    }
}
