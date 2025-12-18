package com.surpass.web.app.controller;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.dto.ApiPageDto;
import com.surpass.entity.app.AppMenu;
import com.surpass.entity.app.dto.AppMenuChangeDto;
import com.surpass.entity.app.dto.AppMenuPageDto;
import com.surpass.persistence.service.AppMenuService;
import com.surpass.validate.AddGroup;
import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 16:51
 */

@RestController
@RequestMapping("/app-menu")
@RequiredArgsConstructor
public class AppMenuController {
    private final AppMenuService appMenuService;

    @GetMapping("/page")
    public Message<JpaPageResults<AppMenu>> page(@ParameterObject AppMenuPageDto dto) {
        return appMenuService.page(dto);
    }

    @GetMapping("/{id}")
    public Message<AppMenu> getById(@PathVariable("id") String id) {
        return Message.ok(appMenuService.get(id));
    }

    @PostMapping
    public Message<String> create(@Validated(value = AddGroup.class) @RequestBody AppMenuChangeDto dto) {
        return appMenuService.save(dto);
    }

}
