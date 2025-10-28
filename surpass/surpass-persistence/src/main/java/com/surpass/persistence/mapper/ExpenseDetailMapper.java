package com.surpass.persistence.mapper;

import com.surpass.entity.expense.ExpenseDetail;
import com.surpass.entity.expense.vo.ExpenseDetailVo;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExpenseDetailMapper extends BaseMapperPlus<ExpenseDetailMapper, ExpenseDetail, ExpenseDetailVo> {

}
