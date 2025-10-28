package com.surpass.persistence.mapper;


import com.surpass.entity.expense.ExpenseApproval;
import com.surpass.entity.expense.vo.ExpenseApprovalVo;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExpenseApprovalMapper extends BaseMapperPlus<ExpenseApprovalMapper, ExpenseApproval, ExpenseApprovalVo> {

}
