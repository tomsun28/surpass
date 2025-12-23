package com.surpass.web.app.controller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClientRelation;
import com.surpass.entity.TreeAttributes;
import com.surpass.entity.TreeNode;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesChangeDto;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.ClientAuthzDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.permissions.Resources;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.persistence.service.RegisteredClientRelationService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.tree.MapTree;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        return Message.ok(appResourcesService.get(id));
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
}
