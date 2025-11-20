package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.DataSource;
import com.surpass.enums.DataSourceStatus;
import org.dromara.mybatis.jpa.service.IJpaService;

import java.util.List;

public interface DataSourceService extends IJpaService<DataSource> {
    Message<String> saveDataSource(DataSource dataSource);

    Message<String> updateDataSource(DataSource dataSource);

    boolean testConnection(DataSource dataSource);

    void updateStatus(String id, DataSourceStatus status);
}
