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

import lombok.*;
import com.surpass.entity.BaseEntity;

import java.io.Serializable;

/**
 * 凭证模板条目 jbx_voucher_template_item
 *
 * @author wuyan
 * {@code @date} 2025-05-08
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_voucher_template_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherTemplateItem extends BaseEntity implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -2142434165037189016L;

	/**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    String id;
    /**
     * 关联编码
     */
    String relatedId;
    /**
     * 模板名称
     */
    String templateId;

    /**
     * 摘要
     */
    String summary;

    /**
     * 科目编码
     */
    String subjectCode;

    /**
     * 余额方向 1 借 2 贷
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Integer direction;
    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
