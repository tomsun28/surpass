package com.surpass.web.expense.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.dto.ExpenseDetailChangeDto;
import com.surpass.entity.expense.dto.ExpenseDetailPageDto;
import com.surpass.entity.expense.vo.ExpenseDetailVo;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.expense.ExpenseDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenseDetail")
@Slf4j
@RequiredArgsConstructor
public class ExpenseDetailController {

    private final ExpenseDetailService expenseDetailService;

    @GetMapping("/get")
    public Message<ExpenseDetailVo> get(@RequestParam("id") String id, @CurrentUser UserInfo userInfo) {
        return expenseDetailService.queryById(id);
    }

    @GetMapping("/fetch")
    public Message<Page<ExpenseDetailVo>> fetch(ExpenseDetailPageDto dto, @CurrentUser UserInfo userInfo) {
        return expenseDetailService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated @RequestBody ExpenseDetailChangeDto dto, @CurrentUser UserInfo userInfo) {
        return expenseDetailService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated @RequestBody ExpenseDetailChangeDto dto) {
        return expenseDetailService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {
        return expenseDetailService.delete(dto);
    }
}
