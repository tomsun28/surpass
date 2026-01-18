package org.dromara.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.api.ApiDataSource;
import org.dromara.surpass.enums.DataSourceStatus;

public interface ApiDataSourceService extends IJpaService<ApiDataSource> {
    Message<String> saveDataSource(ApiDataSource dataSource);

    Message<String> updateDataSource(ApiDataSource dataSource);

    Message<String> deleteDataSource(String id);

    boolean testConnection(ApiDataSource dataSource);

    void updateStatus(String id, DataSourceStatus status);

    javax.sql.DataSource buildDataSource(ApiDataSource cfg);
}
