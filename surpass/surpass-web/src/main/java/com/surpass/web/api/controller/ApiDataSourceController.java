package com.surpass.web.api.controller;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDataSource;
import com.surpass.enums.DataSourceStatus;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.ApiDataSourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/datasources")
@RequiredArgsConstructor
public class ApiDataSourceController {

    private final ApiDataSourceService dataSourceService;

    @GetMapping
    public Message<List<ApiDataSource>> list() {
        List<ApiDataSource> dataSources = dataSourceService.findAll();
        return Message.ok(dataSources);
    }

    @GetMapping("/{id}")
    public Message<ApiDataSource> getById(@PathVariable String id) {
        return Message.ok(dataSourceService.get(id));
    }

    @PostMapping
    public Message<String> create(@Valid @RequestBody ApiDataSource dataSource) {
        return dataSourceService.saveDataSource(dataSource);
    }

    @PutMapping("/{id}")
    public Message<String> update(@PathVariable String id, @Valid @RequestBody ApiDataSource dataSource) {
        dataSource.setId(id);
        return dataSourceService.updateDataSource(dataSource);
    }

    @DeleteMapping("/{id}")
    public Message<String> delete(@PathVariable String id) {
        return dataSourceService.deleteDataSource(id);
    }

    @PostMapping("/{id}/test")
    public Message<Boolean> testConnection(@PathVariable String id) {
        ApiDataSource dataSource = dataSourceService.get(id);
        if (Objects.isNull(dataSource)) {
            throw new BusinessException(50001, "数据源不存在");
        }
        boolean success = dataSourceService.testConnection(dataSource);
        return Message.ok(success);
    }

    @PostMapping("/test")
    public Message<Boolean> testConnection(@Valid @RequestBody ApiDataSource dataSource) {
        boolean success = dataSourceService.testConnection(dataSource);
        return Message.ok(success);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String id, @RequestParam DataSourceStatus status) {
        dataSourceService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
