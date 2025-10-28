package com.surpass.persistence.service.expense;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseAttachment;
import com.surpass.entity.expense.dto.ExpenseAttachmentChangeDto;
import com.surpass.entity.expense.dto.ExpenseAttachmentPageDto;
import com.surpass.entity.expense.vo.ExpenseAttachmentVo;

public interface ExpenseAttachmentService extends IService<ExpenseAttachment> {

    Message<ExpenseAttachmentVo> queryById(String id);

    Message<Page<ExpenseAttachmentVo>> pageList(ExpenseAttachmentPageDto dto);

    Message<String> save(ExpenseAttachmentChangeDto dto);

    Message<String> update(ExpenseAttachmentChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
