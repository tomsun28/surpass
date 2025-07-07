package org.dromara.surpass.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.dromara.surpass.pojo.dto.Message;
import org.dromara.surpass.pojo.entity.AuthAccountLog;
import org.dromara.surpass.pojo.entity.AuthOperationLog;
import org.dromara.surpass.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tomsun28
 * @date 12:20 2018/4/22
 */
@RestController
@RequestMapping("/log")
@Api(tags = "日志管理")
public class LogController {

    @Autowired
    LogService logService;

    @ApiOperation(value = "获取日志记录", httpMethod = "GET")
    @RequestMapping("/account")
    public ResponseEntity<Message> getAccountLogList(@RequestParam(defaultValue = "0") Integer currentPage,
                                                     @RequestParam(defaultValue = "8") Integer pageSize) {
        Page<AuthAccountLog> accountLogs = logService.getAccountLogs(currentPage, pageSize);
        Message message = Message.builder().data(accountLogs).build();
        return ResponseEntity.ok().body(message);
    }

    @ApiOperation(value = "获取用户操作api日志列表", httpMethod = "GET")
    @RequestMapping("/operation")
    public ResponseEntity<Message> getOperationLogList(@RequestParam(defaultValue = "0") Integer currentPage,
                                                       @RequestParam(defaultValue = "8") Integer pageSize) {
        Page<AuthOperationLog> accountLogs = logService.getOperationLogs(currentPage, pageSize);
        Message message = Message.builder().data(accountLogs).build();
        return ResponseEntity.ok().body(message);
    }
}
