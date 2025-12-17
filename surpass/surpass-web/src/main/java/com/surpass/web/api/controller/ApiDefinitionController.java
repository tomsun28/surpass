package com.surpass.web.api.controller;
import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.dto.ApiPageDto;
import com.surpass.persistence.service.ApiDefinitionService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 24096
 */
@RestController
@RequestMapping("/apis")
@RequiredArgsConstructor
public class ApiDefinitionController {

    private final ApiDefinitionService apiDefinitionService;

    @GetMapping
    public ResponseEntity<List<ApiDefinition>> list() {
        List<ApiDefinition> apis = apiDefinitionService.findAll();
        return ResponseEntity.ok(apis);
    }

    @GetMapping("/page")
    public Message<JpaPageResults<ApiDefinition>> page(@ParameterObject ApiPageDto dto) {

        return apiDefinitionService.page(dto);
    }

    @GetMapping("/{id}")
    public Message<ApiDefinition> getById(@PathVariable("id") String id) {
        return Message.ok(apiDefinitionService.get(id));
    }

    @GetMapping("/datasource/{datasourceId}")
    public Message<List<ApiDefinition>> getByDatasourceId(@PathVariable("datasourceId") String datasourceId) {
        return apiDefinitionService.findByDatasourceId(datasourceId);
    }

    @PostMapping
    public Message<String> create(@Validated(value = AddGroup.class) @RequestBody ApiDefinition apiDefinition) {
        return apiDefinitionService.save(apiDefinition);
    }

    @PutMapping("/{id}")
    public Message<String> update(@PathVariable("id") String id, @Validated(value = EditGroup.class) @RequestBody ApiDefinition apiDefinition) {
        apiDefinition.setId(id);
        return apiDefinitionService.edit(apiDefinition);
    }

    @DeleteMapping(value = {"/delete"})
    public Message<String> delete(@RequestParam("ids") List<String> ids) {
        if (apiDefinitionService.softDelete(ids)) {
            return new Message<>("删除成功");
        } else {
            return new Message<>("删除失败");
        }
    }
}
