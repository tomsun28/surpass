package com.surpass.persistence.service.expense;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseApproval;
import com.surpass.entity.expense.dto.ExpenseApprovalChangeDto;
import com.surpass.entity.expense.dto.ExpenseApprovalPageDto;
import com.surpass.entity.expense.vo.ExpenseApprovalVo;

public interface ExpenseApprovalService extends IService<ExpenseApproval> {

    Message<ExpenseApprovalVo> queryById(String id);

    Message<Page<ExpenseApprovalVo>> pageList(ExpenseApprovalPageDto dto);

    Message<String> save(ExpenseApprovalChangeDto dto);

    Message<String> update(ExpenseApprovalChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
