package org.dromara.surpass.controller;

import lombok.extern.slf4j.Slf4j;

import org.dromara.surpass.pojo.Message;
import org.dromara.surpass.pojo.entity.AuthResource;
import org.dromara.surpass.pojo.entity.AuthRole;
import org.dromara.surpass.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * @author tomsun28
 * @date 00:24 2019-08-01
 */
@RestController
@RequestMapping("/role")
@Slf4j
@Tag(name  = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取角色拥有的资源")
    @GetMapping("/resource/{roleId}")
    public ResponseEntity<Message> getResourceOwnByRole(@PathVariable Long roleId,
                                                        @RequestParam(defaultValue = "0") Integer currentPage,
                                                        @RequestParam(defaultValue = "8") Integer pageSize) {
        Page<AuthResource> resourcePage = roleService.getPageResourceOwnRole(roleId, currentPage, pageSize);
        Message message = Message.builder().data(resourcePage).build();
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "获取角色未拥有的资源")
    @GetMapping("/resource/-/{roleId}")
    public ResponseEntity<Message> getResourceNotOwnByRole(@PathVariable Long roleId,
                                                           @RequestParam(defaultValue = "0") Integer currentPage,
                                                           @RequestParam(defaultValue = "8") Integer pageSize) {
        Page<AuthResource> resourcePage = roleService.getPageResourceNotOwnRole(roleId, currentPage, pageSize);
        Message message = Message.builder().data(resourcePage).build();
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "赋权角色资源")
    @PostMapping("/authority/resource/{roleId}/{resourceId}")
    public ResponseEntity<Message> authorityRoleResource(@PathVariable Long roleId,
                                                         @PathVariable Long resourceId) {
        roleService.authorityRoleResource(roleId,resourceId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "取消赋权角色资源")
    @DeleteMapping("/authority/resource/{roleId}/{resourceId}")
    public ResponseEntity<Message> deleteAuthorityRoleResource(@PathVariable Long roleId,
                                                         @PathVariable Long resourceId) {
        roleService.deleteAuthorityRoleResource(roleId,resourceId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public ResponseEntity<Message> addRole(@RequestBody @Validated AuthRole authRole) {
        if (roleService.addRole(authRole)) {
            if (log.isDebugEnabled()) {
                log.debug("add role success: {}", authRole);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            Message message = Message.builder()
                    .msg("role already exist").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
    }

    @Operation(summary = "更新资源")
    @PutMapping
    public ResponseEntity<Message> updateRole(@RequestBody @Validated AuthRole authRole) {
        if (roleService.updateRole(authRole)) {
            if (log.isDebugEnabled()) {
                log.debug("update role success: {}", authRole);
            }
            return ResponseEntity.ok().build();
        } else {
            Message message = Message.builder()
                    .msg("role not exist").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
    }

    @Operation(summary = "删除资源")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Message> deleteRole(@PathVariable Long roleId) {
        if (roleService.deleteRole(roleId)) {
            if (log.isDebugEnabled()) {
                log.debug("delete role success: {}", roleId);
            }
            return ResponseEntity.ok().build();
        } else {
            Message message = Message.builder()
                    .msg("delete role fail, no this role here").build();
            log.debug("delete role fail: {}", roleId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
    }

    @Operation(summary = "获取资源")
    @GetMapping
    public ResponseEntity<Message> getRole(@RequestParam(defaultValue = "0") Integer currentPage,
                                           @RequestParam(defaultValue = "8") Integer pageSize) {
        Page<AuthRole> rolePage = roleService.getPageRole(currentPage, pageSize);
        Message message = Message.builder().data(rolePage).build();
        return ResponseEntity.ok().body(message);
    }

}
