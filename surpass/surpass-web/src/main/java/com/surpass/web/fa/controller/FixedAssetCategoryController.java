package com.surpass.web.fa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssetCategory;
import com.surpass.entity.fa.dto.FixedAssetCategoryChangeDto;
import com.surpass.entity.fa.dto.FixedAssetCategoryPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.FixedAssetCategoryService;
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
 * @time: 2025/7/1 10:09
 */

@RestController
@RequestMapping("/fa/category")
@Slf4j
@RequiredArgsConstructor
public class FixedAssetCategoryController {
    private final FixedAssetCategoryService fixedAssetCategoryService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<FixedAssetCategory>> fetch(@ParameterObject FixedAssetCategoryPageDto dto,
                                                   @CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setBookId(currentUser.getBookId());
        return fixedAssetCategoryService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<FixedAssetCategory> getById(@PathVariable(name = "id") String id) {
        return Message.ok(fixedAssetCategoryService.getById(id));
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody FixedAssetCategoryChangeDto dto,
                                @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return fixedAssetCategoryService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody FixedAssetCategoryChangeDto dto) {
        return fixedAssetCategoryService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return fixedAssetCategoryService.delete(dto);
    }
}
