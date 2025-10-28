package com.surpass.entity.fa;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/3 14:32
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_fa_assets")
@Data
public class FixedAssets extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1230610981694904111L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    private String usageDepartment;

    /**
     * 备注
     */
    private String remark;

    private String bookId;

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

    /**
     * 新增资产凭证ID
     */
    private String assetVoucherId;

    /**
     * 新增资产凭证ID
     */
    private String cleanUpVoucherId;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;


    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String assetSubjectName;

    @TableField(exist = false)
    private String depreciationSubjectName;

    @TableField(exist = false)
    private String expenseSubjectName;

    @TableField(exist = false)
    private String purchaserSubjectName;

    @TableField(exist = false)
    private String liquidationSubjectName;

    @TableField(exist = false)
    private String departmentName;

    @TableField(exist = false)
    private String custodianName;

    @TableField(exist = false)
    private String currentTerm;

    @TableField(exist = false)
    private List<String> usageDepartmentList;

    public List<String> getUsageDepartmentList() {
        if (StringUtils.isNotBlank(usageDepartment)) {
            return Arrays.asList(usageDepartment.split(","));
        }
        return new ArrayList<>();
    }

    public void setUsageDepartmentList(List<String> list) {
        this.usageDepartmentList = list;
        // 可选：同步回字符串字段
        this.usageDepartment = String.join(",", list);
    }
}
