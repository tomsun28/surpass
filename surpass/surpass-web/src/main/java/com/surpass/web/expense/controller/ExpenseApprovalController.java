package com.surpass.web.expense.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.dto.ExpenseApprovalChangeDto;
import com.surpass.entity.expense.dto.ExpenseApprovalPageDto;
import com.surpass.entity.expense.vo.ExpenseApprovalVo;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.expense.ExpenseApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 22:14:31
 * {@code @version} 1.0
 */

@RestController
@RequestMapping("/expenseApproval")
@Slf4j
@RequiredArgsConstructor
public class ExpenseApprovalController {

    private final ExpenseApprovalService expenseApprovalService;

    @GetMapping("/get")
    public Message<ExpenseApprovalVo> get(@RequestParam("id") String id, @CurrentUser UserInfo userInfo) {
        return expenseApprovalService.queryById(id);
    }

    @GetMapping("/fetch")
    public Message<Page<ExpenseApprovalVo>> fetch(ExpenseApprovalPageDto dto, @CurrentUser UserInfo userInfo) {
        return expenseApprovalService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated @RequestBody ExpenseApprovalChangeDto dto, @CurrentUser UserInfo userInfo) {
        return expenseApprovalService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated @RequestBody ExpenseApprovalChangeDto dto) {
        return expenseApprovalService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {
        return expenseApprovalService.delete(dto);
    }
}
