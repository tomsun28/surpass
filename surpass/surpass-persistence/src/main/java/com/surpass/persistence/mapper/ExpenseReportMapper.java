package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.expense.ExpenseReport;
import com.surpass.entity.expense.dto.ExpenseReportPageDto;
import com.surpass.entity.expense.vo.ExpenseReportVo;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseReportMapper extends BaseMapperPlus<ExpenseReportMapper, ExpenseReport, ExpenseReportVo> {

    Page<ExpenseReportVo> selectVoPageList(@Param("page") Page<ExpenseReport> page, @Param("dto") ExpenseReportPageDto dto);
}
