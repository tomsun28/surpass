package com.surpass.entity.fa;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/30 17:15
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_fa_categories")
@Data
public class FixedAssetCategory extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -727333434704482392L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 固定资产科目
     */
    private String assetSubjectCode;

    /**
     * 累计折旧科目
     */
    private String depreciationSubjectCode;

    /**
     * 折旧费用科目
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

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    private String bookId;

    @TableField(exist = false)
    private String assetSubjectName;

    @TableField(exist = false)
    private String depreciationSubjectName;

    @TableField(exist = false)
    private String expenseSubjectName;
}
