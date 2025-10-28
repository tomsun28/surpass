package com.surpass.persistence.service.expense;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpensePayment;
import com.surpass.entity.expense.dto.ExpensePaymentChangeDto;
import com.surpass.entity.expense.dto.ExpensePaymentPageDto;
import com.surpass.entity.expense.vo.ExpensePaymentVo;

public interface ExpensePaymentService extends IService<ExpensePayment> {

    Message<ExpensePaymentVo> queryById(String id);

    Message<Page<ExpensePaymentVo>> pageList(ExpensePaymentPageDto dto);

    Message<String> save(ExpensePaymentChangeDto dto);

    Message<String> update(ExpensePaymentChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
