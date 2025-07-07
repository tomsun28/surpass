package org.dromara.surpass.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.dromara.surpass.pojo.dto.Message;
import org.dromara.surpass.pojo.entity.AuthResource;
import org.dromara.surpass.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author tomsun28
 * @date 00:24 2019-08-01
 */
@RequestMapping("/resource")
@RestController
@Slf4j
@Api(tags = "资源管理")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "新增资源")
    @PostMapping
    public ResponseEntity<Message> addResource(@RequestBody @Validated AuthResource authResource) {
        if (resourceService.addResource(authResource)) {
            if (log.isDebugEnabled()) {
                log.debug("add resource success: {}", authResource);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            Message message = Message.builder().msg("resource already exist").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
    }

    @ApiOperation(value = "更新资源")
    @PutMapping
    public ResponseEntity<Message> updateResource(@RequestBody @Validated AuthResource authResource) {
        if (resourceService.updateResource(authResource)) {
            if (log.isDebugEnabled()) {
                log.debug("update resource success: {}", authResource);
            }
            return ResponseEntity.ok().build();
        } else {
            Message message = Message.builder().msg("resource not exist").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Message> deleteResource(@PathVariable Long resourceId ) {
        if (resourceService.deleteResource(resourceId)) {
            if (log.isDebugEnabled()) {
                log.debug("delete resource success: {}", resourceId);
            }
            return ResponseEntity.ok().build();
        } else {
            Message message = Message.builder().msg("delete resource fail, please try again later").build();
            log.error("delete resource fail: {}", resourceId);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(message);
        }
    }

    @ApiOperation(value = "获取资源")
    @GetMapping
    public ResponseEntity<Message> getResource(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "8") Integer pageSize) {
        Page<AuthResource> resourcePage = resourceService.getPageResource(currentPage, pageSize);
        Message message = Message.builder().data(resourcePage.get()).build();
        return ResponseEntity.ok().body(message);
    }

}
