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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 利润表导出模板数据
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementIncomeExport implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -324664646653443654L;
	private String companyName;
    private String date;
    private List<Item> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String itemName;
        private Integer rowNum;

        @ExcelExportCfg
        private BigDecimal currentBalance;
        @ExcelExportCfg
        private BigDecimal yearBalance;

    }
}
