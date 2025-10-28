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


package com.surpass.entity.voucher;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.util.excel.ExcelExportCfg;
import lombok.*;
import com.surpass.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 凭证模板 jbx_voucher_template
 *
 * @author wuyan
 * {@code @date} 2025-05-08
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_voucher_template")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherTemplate extends BaseEntity implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -3028372120266615619L;

	/**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    String id;

    /**
     * 关联编码
     */
    String relatedId;

    String code;

    /**
     * 名称
     */
    String name;
    /**
     * 类型 1 期末处理 2 薪资 3 日记账
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Integer category;

    /**
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Integer voucherType;

    /**
     * 默认凭证日期，为月份的第几天，0为月末
     */
    Integer voucherDate;

    /**
     * 字头：“记”、“收”、“付”、“转”等
     */
    String wordHead;

    /**
     * 备注
     */
    String remark;
    /**
     * 排序
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Integer sortIndex;

    /**
     * 状态
     */
    @ExcelExportCfg(enumClass = VoucherStatusEnum.class)
    String status;



    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;


    /**
     * 模板的条目
     */
    @TableField(exist = false)
    private List<VoucherTemplateItem> items;
}
