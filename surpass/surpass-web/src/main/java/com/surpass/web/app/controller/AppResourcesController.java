package com.surpass.web.app.controller;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesChangeDto;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.validate.AddGroup;
import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/18 17:08
 */

@RestController
@RequestMapping("/app-resources")
@RequiredArgsConstructor
public class AppResourcesController {
    private final AppResourcesService appResourcesService;

    @PostMapping("/add")
    public Message<String> addApp(@Validated(value = AddGroup.class) @RequestBody AppResourcesChangeDto dto) {
        return appResourcesService.create(dto);
    }

    @GetMapping("/page")
    public Message<JpaPageResults<AppResources>> page(@ParameterObject AppResourcesPageDto dto) {

        return appResourcesService.page(dto);
    }
}
