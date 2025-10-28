package com.surpass.entity.invoice.dto;

import com.surpass.entity.invoice.InvoiceVoucherItem;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/28 17:35
 */

@Data
public class InvoiceVoucherChangeDto {
    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    private String bookId;

    @NotEmpty(message = "模板名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String templateName;

    @NotEmpty(message = "发票方向不能为空", groups = {AddGroup.class, EditGroup.class})
    private String direction;

    @NotEmpty(message = "凭证字不能为空", groups = {AddGroup.class, EditGroup.class})
    private String wordHead;

    @NotNull(message = "模板状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;

    @NotNull(message = "是否为默认模板设定不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isDefault;

    @Valid
    private List<InvoiceVoucherItem> items;
}
