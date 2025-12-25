package com.surpass.web.app.controller;

import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClientRelation;
import com.surpass.entity.app.App;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesChangeDto;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.ClientAuthzDto;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.persistence.service.AppService;
import com.surpass.persistence.service.RegisteredClientRelationService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.tree.MapTree;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private final AppService appService;

    private final RegisteredClientRelationService registeredClientRelationService;

    @PostMapping("/add")
    public Message<String> addResources(@Validated(value = AddGroup.class) @RequestBody AppResourcesChangeDto dto) {
        return appResourcesService.create(dto);
    }

    @GetMapping("/page")
    public Message<JpaPageResults<AppResources>> page(@ParameterObject AppResourcesPageDto dto) {

        return appResourcesService.page(dto);
    }

    @PutMapping("/update")
    public Message<String> updateResources(@Validated(value = EditGroup.class) @RequestBody AppResourcesChangeDto dto) {

        return appResourcesService.updateResources(dto);
    }

    @GetMapping("/get/{id}")
    public Message<AppResources> get(@PathVariable String id) {
        AppResources appResources = appResourcesService.get(id);
        if (Objects.nonNull(appResources)) {
            App app = appService.get(appResources.getAppId());
            if (Objects.nonNull(app)) {
                appResources.setContextPath(app.getContextPath());
                appResources.setBelongApp(app.getAppName());
            }
        }
        return Message.ok(appResources);
    }

    @DeleteMapping(value = {"/delete"})
    public Message<String> delete(@RequestParam("ids") List<String> ids) {
        if (appResourcesService.softDelete(ids)) {
            return new Message<>("删除成功");
        } else {
            return new Message<>("删除失败");
        }
    }

    @GetMapping(value={"/tree"})
    public Message<Map<String, List<MapTree<String>>>> tree(@ParameterObject AppResourcesPageDto dto) {
       return Message.ok(appResourcesService.tree(dto));
    }

    @PostMapping("/clientAuthz")
    public Message<String> clientAuthz(@Validated @RequestBody ClientAuthzDto dto) {
        return registeredClientRelationService.saveClientAppRelation(dto);
    }

    @GetMapping("/getClientAuthz")
    public Message<List<RegisteredClientRelation>> getClientAuthz(@ParameterObject AppResourcesPageDto dto) {
        return registeredClientRelationService.getClientAuthz(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AppResources>> list() {
        LambdaQuery<AppResources> wrapper = new LambdaQuery<>();
        wrapper.eq(AppResources::getClassify, "openApi");
        List<AppResources> apis = appResourcesService.query(wrapper);
        return ResponseEntity.ok(apis);
    }
}
