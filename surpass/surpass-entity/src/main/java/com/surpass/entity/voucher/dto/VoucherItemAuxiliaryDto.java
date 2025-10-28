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


package com.surpass.entity.voucher.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 简介说明: 辅助核算配置实体类
 *
 * @author wuyan
 * {@code @date} 2025/02/23 14:29:58
 * {@code @version} 1.0
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherItemAuxiliaryDto {
    /**
     * 辅助核算对象类型
     */
    @NotNull(message = "辅助核算类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String id;

    /**
     * 辅助核算对象名称
     */
    @NotNull(message = "辅助核算名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String label;

    /**
     * 辅助核算对象列表
     */
    private List<BooksVoucherItemAuxiliaryValue> value;

    /**
     * 辅助核算对象配置信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BooksVoucherItemAuxiliaryValue {
        /**
         * 对象名称
         */
        @NotNull(message = "辅助核算对象不能为空", groups = {AddGroup.class, EditGroup.class})
        private String label;

        /**
         * 对象ID
         */
        @NotNull(message = "辅助核算对象不能为空", groups = {AddGroup.class, EditGroup.class})
        private String value;
    }
}
