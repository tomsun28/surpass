package com.surpass.web.invoice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.FixedAssetsChangeDto;
import com.surpass.entity.fa.dto.FixedAssetsPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.dto.InvoiceChangeDto;
import com.surpass.entity.invoice.dto.InvoicePageDto;
import com.surpass.entity.invoice.vo.InvoiceVo;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.persistence.service.InvoiceService;
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
 * @time: 2025/8/26 17:45
 */

@RestController
@RequestMapping("/invoice")
@Slf4j
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<Invoice>> fetch(@ParameterObject InvoicePageDto dto,
                                        @CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setBookId(currentUser.getBookId());
        return invoiceService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<InvoiceVo> getById(@PathVariable(name = "id") String id) {
        return invoiceService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody InvoiceChangeDto dto,
                                @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return invoiceService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody InvoiceChangeDto dto) {
        return invoiceService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return invoiceService.delete(dto);
    }

    @PostMapping("/generate-voucher")
    public Message<String> generateVoucher(@RequestBody GenerateVoucherDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return invoiceService.generateVoucher(dto);
    }

    @DeleteMapping("/delete-voucher")
    public Message<String> deleteVoucher(@RequestBody GenerateVoucherDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return invoiceService.deleteVoucher(dto);
    }
}
