package com.surpass.entity.invoice;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/28 11:11
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_invoice_voucher_item")
@Data
public class InvoiceVoucherItem extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3252389139104311210L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 分录顺序号
     */
    private Integer seqNo;

    /**
     * 借贷方向
     */
    @NotEmpty(message = "借贷方向不能为空", groups = {AddGroup.class, EditGroup.class})
    private String direction;

    /**
     * 科目代码
     */
    @NotEmpty(message = "科目代码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String subjectCode;

    /**
     * 摘要
     */
    @NotEmpty(message = "摘要不能为空", groups = {AddGroup.class, EditGroup.class})
    private String summary;

    /**
     * 取值
     */
    @NotEmpty(message = "取值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String selectedValue;
}
