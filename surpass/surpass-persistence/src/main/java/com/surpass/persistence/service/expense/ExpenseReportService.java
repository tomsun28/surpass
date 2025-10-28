package com.surpass.persistence.service.expense;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseReport;
import com.surpass.entity.expense.dto.ExpenseReportChangeDto;
import com.surpass.entity.expense.dto.ExpenseReportPageDto;
import com.surpass.entity.expense.vo.ExpenseReportVo;

public interface ExpenseReportService extends IService<ExpenseReport> {

    Message<ExpenseReportVo> queryById(String id);

    Message<Page<ExpenseReportVo>> pageList(ExpenseReportPageDto dto);

    Message<String> save(ExpenseReportChangeDto dto);

    Message<String> update(ExpenseReportChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
