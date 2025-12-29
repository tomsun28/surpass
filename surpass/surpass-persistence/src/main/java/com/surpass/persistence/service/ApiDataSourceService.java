package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDataSource;
import com.surpass.enums.DataSourceStatus;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface ApiDataSourceService extends IJpaService<ApiDataSource> {
    Message<String> saveDataSource(ApiDataSource dataSource);

    Message<String> updateDataSource(ApiDataSource dataSource);

    Message<String> deleteDataSource(String id);

    boolean testConnection(ApiDataSource dataSource);

    void updateStatus(String id, DataSourceStatus status);

    javax.sql.DataSource buildDataSource(ApiDataSource cfg);
}
