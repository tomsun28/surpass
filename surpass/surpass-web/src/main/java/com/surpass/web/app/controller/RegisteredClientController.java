package com.surpass.web.app.controller;

import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.entity.RegisteredClientRelation;
import com.surpass.entity.app.App;
import com.surpass.entity.app.dto.*;
import com.surpass.entity.dto.RegisteredClientChangeDto;
import com.surpass.entity.dto.RegisteredClientPageDto;
import com.surpass.entity.dto.RegisteredClientRelationDto;
import com.surpass.persistence.service.RegisteredClientRelationService;
import com.surpass.persistence.service.RegisteredClientService;
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
public class RegisteredClientController {

    private final RegisteredClientService appClientService;

    private final RegisteredClientRelationService appClientRelationService;

    @PostMapping("/add")
    public Message<String> addApp(@Validated(value = AddGroup.class) @RequestBody RegisteredClientChangeDto dto) {
        return appClientService.create(dto);
    }

    @PutMapping("/update")
    public Message<String> updateApp(@Validated(value = EditGroup.class) @RequestBody RegisteredClientChangeDto dto) {
        return appClientService.updateApp(dto);
    }

    @GetMapping("/get/{id}")
    public Message<RegisteredClient> get(@PathVariable("id") String id) {
        return Message.ok(appClientService.get(id));
    }

    @DeleteMapping(value = {"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<RegisteredClient> delete(@RequestParam("ids") List<String> ids) {
        if (appClientService.softDelete(ids)) {
            return new Message<>(Message.SUCCESS);
        } else {
            return new Message<>(Message.FAIL);
        }
    }

    @GetMapping("/list")
    public Message<JpaPageResults<RegisteredClient>> pageList(@ParameterObject RegisteredClientPageDto dto) {
        return Message.ok(appClientService.fetchPageResults(dto));
    }

    @GetMapping("/relate-app/{clientId}")
    public Message<List<RegisteredClientRelation>> getClientApps(@PathVariable("clientId") String clientId) {
        return Message.ok(appClientRelationService.getClientApps(clientId));
    }

    @PostMapping("/save-relate")
    public Message<String> saveClientAppRelation(@Validated @RequestBody RegisteredClientRelationDto dto) {
        return appClientRelationService.saveClientAppRelation(dto);
    }
}
