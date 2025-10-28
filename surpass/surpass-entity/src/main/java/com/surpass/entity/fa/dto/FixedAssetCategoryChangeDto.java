package com.surpass.entity.fa.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/1 10:21
 */

@Data
public class FixedAssetCategoryChangeDto {
    /**
     * 主键
     */
    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 编码
     */
    private String categoryCode;

    /**
     * 名称
     */
    private String categoryName;

    /**
     * 固定资产科目ID
     */
    private String assetSubjectCode;

    /**
     * 累计折旧科目ID
     */
    private String depreciationSubjectCode;

    /**
     * 折旧费用科目ID
     */
    private String expenseSubjectCode;

    /**
     * 折旧方法：1=平均年限法，2=双倍余额递减法，3=不提折旧
     */
    private Integer depreciationMethod;

    /**
     * 预计使用年限
     */
    private Integer depreciationYears;

    /**
     * 残值率
     */
    private BigDecimal residualRate;

    /**
     * 备注
     */
    private String remark;


    private String bookId;
}
