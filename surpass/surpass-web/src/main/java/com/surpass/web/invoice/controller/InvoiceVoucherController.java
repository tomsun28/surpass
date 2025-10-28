package com.surpass.web.invoice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.invoice.InvoiceVoucherTemplate;
import com.surpass.entity.invoice.dto.InvoiceChangeDto;
import com.surpass.entity.invoice.dto.InvoiceDefaultDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherChangeDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherPageDto;
import com.surpass.entity.invoice.vo.InvoiceVoucherVo;
import com.surpass.persistence.service.InvoiceVoucherTemplateService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/28 10:25
 */
@RestController
@RequestMapping("/invoice-voucher")
@Slf4j
@RequiredArgsConstructor
public class InvoiceVoucherController {
    private final InvoiceVoucherTemplateService invoiceVoucherTemplateService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<InvoiceVoucherTemplate>> fetch(@ParameterObject InvoiceVoucherPageDto dto,
                                                       @CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setBookId(currentUser.getBookId());
        return invoiceVoucherTemplateService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<InvoiceVoucherVo> getById(@PathVariable(name = "id") String id) {
        return invoiceVoucherTemplateService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody InvoiceVoucherChangeDto dto,
                                @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return invoiceVoucherTemplateService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody InvoiceVoucherChangeDto dto) {
        return invoiceVoucherTemplateService.update(dto);
    }


    @PutMapping("/set-default")
    public Message<String> setDefault(@Validated @RequestBody InvoiceDefaultDto dto,
                                      @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return invoiceVoucherTemplateService.setDefault(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return invoiceVoucherTemplateService.delete(dto);
    }
}
