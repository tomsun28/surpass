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

import com.surpass.entity.voucher.VoucherTemplateItem;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 凭证模板编辑对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@Data
public class VoucherTemplateChangeDto {
    /**
     * 主键
     */
    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    String id;

    @NotEmpty(message = "模板名称不能为空", groups = {AddGroup.class, EditGroup.class})
    String name;

    String code;

    Integer category;
    /**
     * 字头：“收”、“付”、“转”等
     */
    @NotNull(message = "凭证字头不能为空", groups = {AddGroup.class})
    String wordHead;

    /**
     * 默认凭证日期，为月份的第几天，0为月末
     */
    Integer voucherDate;

    Integer voucherType;

    /**
     * 所属账套
     */
    @NotEmpty(message = "关联对象编码不能为空", groups = {AddGroup.class, EditGroup.class})
    String relatedId;

    /**
     * 备注
     */
    String remark;
    /**
     * 排序
     */
    Integer sortIndex;

    /**
     * 状态：暂存 - draft,审核中 - reviewing，已完成 - completed，被拒绝 - rejected，已取消 - cancelled
     */
    String status;

    /**
     * 凭证明细记录
     */
    @NotNull(message = "凭证明细不能为空", groups = {AddGroup.class, EditGroup.class})
    private List<VoucherTemplateItem> items;
}
