package com.surpass.web.expense.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.expense.ExpenseReport;
import com.surpass.entity.expense.dto.ExpenseReportChangeDto;
import com.surpass.entity.expense.dto.ExpenseReportPageDto;
import com.surpass.entity.expense.vo.ExpenseReportVo;
import com.surpass.persistence.service.expense.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenseReport")
@Slf4j
@RequiredArgsConstructor
public class ExpenseReportController {

    private final ExpenseReportService expenseReportService;

    @GetMapping("/get")
    public Message<ExpenseReportVo> get(@RequestParam("id") String id, @CurrentUser UserInfo userInfo) {
        return expenseReportService.queryById(id);
    }

    @GetMapping("/fetch")
    public Message<Page<ExpenseReportVo>> fetch(ExpenseReportPageDto dto, @CurrentUser UserInfo userInfo) {
        return expenseReportService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated @RequestBody ExpenseReportChangeDto dto, @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        return expenseReportService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated @RequestBody ExpenseReportChangeDto dto) {
        return expenseReportService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {
        return expenseReportService.delete(dto);
    }
}
