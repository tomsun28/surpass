package com.surpass.entity.fa.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/3 15:58
 */

@Data
public class FixedAssetsChangeDto {
    private String bookId;

    /**
     * 主键
     */
    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 资产编号
     */
    private String code;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 资产分类ID，关联fa_categories
     */
    private String categoryId;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 购置日期
     */
    private LocalDate purchaseDate;

    /**
     * 原值
     */
    private BigDecimal originalValue;
    /**
     * 残值率%
     */
    private BigDecimal residualRate;
    /**
     * 净值（原值-累计折旧）
     */
    private BigDecimal netValue;

    /**
     * 折旧方法：1=平均年限法，2=双倍余额递减法，3=不提折旧
     */
    private Integer depreciationMethod;

    /**
     * 预计使用期数（期间）
     */
    private Integer depreciationTerms;

    /**
     * 折旧开始日期
     */
    private LocalDate depreciationStartDate;

    /**
     * 资产状态，如 active(使用中), disposal(处置中), disposed(已处置)
     */
    private String assetStatus;

    /**
     * 存放地点
     */
    private String location;

    /**
     * 保管人
     */
    private String custodian;

    /**
     * 使用部门
     */
    @Size(max = 10, message = "最多只能选择10个部门")
    private List<String> usageDepartmentList;

    /**
     * 备注
     */
    private String remark;

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
     * 资产购入对方科目
     */
    private String purchaserSubjectCode;

    /**
     * 资产清理科目
     */
    private String liquidationSubjectCode;
}
