package com.surpass.persistence.mapper;


import com.surpass.entity.expense.ExpenseAttachment;
import com.surpass.entity.expense.vo.ExpenseAttachmentVo;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExpenseAttachmentMapper extends BaseMapperPlus<ExpenseAttachmentMapper, ExpenseAttachment, ExpenseAttachmentVo> {

}
