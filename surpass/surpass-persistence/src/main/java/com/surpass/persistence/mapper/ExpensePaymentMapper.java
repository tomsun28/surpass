package com.surpass.persistence.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.expense.ExpensePayment;
import com.surpass.entity.expense.dto.ExpensePaymentPageDto;
import com.surpass.entity.expense.vo.ExpensePaymentVo;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpensePaymentMapper extends BaseMapperPlus<ExpensePaymentMapper, ExpensePayment, ExpensePaymentVo> {

    Page<ExpensePaymentVo> selectVoPageList(@Param("build") Page<ExpensePayment> build, @Param("dto") ExpensePaymentPageDto dto);
}
