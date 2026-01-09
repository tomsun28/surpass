package org.maxkey.surpass.web.app.controller;

import lombok.RequiredArgsConstructor;

import org.maxkey.surpass.entity.ClientPermission;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.RoleAppResourcesPermission;
import org.maxkey.surpass.entity.app.dto.AppResourcesPageDto;
import org.maxkey.surpass.entity.app.dto.ClientAuthzDto;
import org.maxkey.surpass.entity.app.dto.RoleAppResourcesAuthzDto;
import org.maxkey.surpass.persistence.service.RoleAppResourcesPermissionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/29 15:14
 */

@RestController
@RequestMapping("/role-app-resources")
@RequiredArgsConstructor
public class RoleAppResourcesPermissionController {

    private final RoleAppResourcesPermissionService roleAppResourcesPermissionService;

    @PostMapping("/authz")
    public Message<String> saveRoleAppRelation(@Validated @RequestBody RoleAppResourcesAuthzDto dto) {
        return roleAppResourcesPermissionService.saveRoleAppRelation(dto);
    }

    @GetMapping("/getRoleAuthz")
    public Message<List<RoleAppResourcesPermission>> getRoleAuthz(@ParameterObject AppResourcesPageDto dto) {
        return roleAppResourcesPermissionService.getRoleAuthz(dto);
    }
}
