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
 * @time: 2025/7/9 10:48
 */


@EqualsAndHashCode(callSuper = true)
@TableName("jbx_fa_depreciation_records")
@Data
public class FaDepreciationRecord extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -403000201403451142L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 资产ID
     */
    private String assetId;

    /**
     * 折旧期间
     */
    private String period;

    /**
     * 本期折旧金额
     */
    private BigDecimal depreciationAmount;

    /**
     * 累计折旧金额
     */
    private BigDecimal accumulatedDepreciation;

    /**
     * 折旧后净值
     */
    private BigDecimal netValue;

    /**
     * 折旧方法
     */
    private Integer depreciationMethod;

    /**
     * 账套ID
     */
    private String bookId;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
