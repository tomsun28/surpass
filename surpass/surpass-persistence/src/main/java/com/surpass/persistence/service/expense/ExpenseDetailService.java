package com.surpass.persistence.service.expense;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseDetail;
import com.surpass.entity.expense.dto.ExpenseDetailChangeDto;
import com.surpass.entity.expense.dto.ExpenseDetailPageDto;
import com.surpass.entity.expense.vo.ExpenseDetailVo;

public interface ExpenseDetailService extends IService<ExpenseDetail> {

    Message<ExpenseDetailVo> queryById(String id);

    Message<Page<ExpenseDetailVo>> pageList(ExpenseDetailPageDto dto);

    Message<String> save(ExpenseDetailChangeDto dto);

    Message<String> update(ExpenseDetailChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
