package com.surpass.web.app.controller;

import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import com.surpass.entity.app.AppClient;
import com.surpass.entity.app.dto.AppChangeDto;
import com.surpass.entity.app.dto.AppClientChangeDto;
import com.surpass.entity.app.dto.AppClientPageDto;
import com.surpass.entity.app.dto.AppPageDto;
import com.surpass.persistence.service.AppClientService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/9 16:51
 */

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class AppClientController {

    private final AppClientService appClientService;

    @PostMapping("/add")
    public Message<String> addApp(@Validated(value = AddGroup.class) @RequestBody AppClientChangeDto dto) {
        return appClientService.create(dto);
    }

    @PutMapping("/update")
    public Message<String> updateApp(@Validated(value = EditGroup.class) @RequestBody AppClientChangeDto dto) {
        return appClientService.updateApp(dto);
    }

    @GetMapping("/get/{id}")
    public Message<AppClient> get(@PathVariable("id") String id) {
        return Message.ok(appClientService.get(id));
    }

    @DeleteMapping(value = {"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<App> delete(@RequestParam("ids") List<String> ids) {
        if (appClientService.softDelete(ids)) {
            return new Message<>(Message.SUCCESS);
        } else {
            return new Message<>(Message.FAIL);
        }
    }

    @GetMapping("/list")
    public Message<JpaPageResults<AppClient>> pageList(@ParameterObject AppClientPageDto dto) {
        return Message.ok(appClientService.fetchPageResults(dto));
    }
}
