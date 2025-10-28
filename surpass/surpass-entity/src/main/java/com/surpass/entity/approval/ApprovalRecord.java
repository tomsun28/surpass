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


package com.surpass.entity.approval;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.*;

/**
 * 审批记录实体类 jbx_approval_record
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_approval_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRecord extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 审批单ID，凭证ID
     */
    private String voucherId;

    /**
     * 审批人ID
     */
    private String approverId;

    /**
     * 是否转派
     */
    private String isTransfer;

    /**
     * 审批状态
     * PENDING: 待审批
     * APPROVED: 已批准
     * REJECTED: 已拒绝
     */
    private String status;

    /**
     * 审批人评论
     */
    private String comment;

    /**
     * 删除标记，默认为 'n' (未删除)，如果为 'y' 表示已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
