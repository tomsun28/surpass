package com.surpass.web.fa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.FixedAssetsChangeDto;
import com.surpass.entity.fa.dto.FixedAssetsPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.persistence.service.FixedAssetsService;
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
 * @time: 2025/7/3 15:52
 */

@RestController
@RequestMapping("/fa/info")
@Slf4j
@RequiredArgsConstructor
public class FixedAssetsController {

    private final FixedAssetsService fixedAssetsService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<FixedAssets>> fetch(@ParameterObject FixedAssetsPageDto dto,
                                            @CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setBookId(currentUser.getBookId());
        return fixedAssetsService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<FixedAssets> getById(@PathVariable(name = "id") String id) {
        return fixedAssetsService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody FixedAssetsChangeDto dto,
                                @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return fixedAssetsService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody FixedAssetsChangeDto dto) {
        return fixedAssetsService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return fixedAssetsService.delete(dto);
    }

    @PostMapping("/generate-voucher")
    public Message<String> generateVoucher(@Validated @RequestBody GenerateVoucherDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return fixedAssetsService.generateVoucher(dto);
    }

    @DeleteMapping("/delete-voucher")
    public Message<String> deleteVoucher(@Validated @RequestBody GenerateVoucherDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return fixedAssetsService.deleteVoucher(dto);
    }
}
