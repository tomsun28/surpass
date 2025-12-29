package com.surpass.web.app.controller;

import com.surpass.entity.ClientPermission;
import com.surpass.entity.Message;
import com.surpass.entity.RoleAppResourcesPermission;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.ClientAuthzDto;
import com.surpass.entity.app.dto.RoleAppResourcesAuthzDto;
import com.surpass.persistence.service.RoleAppResourcesPermissionService;
import lombok.RequiredArgsConstructor;
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
    private RoleAppResourcesPermissionService roleAppResourcesPermissionService;

    @PostMapping("/authz")
    public Message<String> saveRoleAppRelation(@Validated @RequestBody RoleAppResourcesAuthzDto dto) {
        return roleAppResourcesPermissionService.saveRoleAppRelation(dto);
    }

    @GetMapping("/getRoleAuthz")
    public Message<List<RoleAppResourcesPermission>> getRoleAuthz(@ParameterObject AppResourcesPageDto dto) {
        return roleAppResourcesPermissionService.getRoleAuthz(dto);
    }
}
