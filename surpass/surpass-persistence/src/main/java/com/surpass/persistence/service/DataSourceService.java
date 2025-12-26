package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.api.DataSource;
import com.surpass.enums.DataSourceStatus;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface DataSourceService extends IJpaService<DataSource> {
    Message<String> saveDataSource(DataSource dataSource);

    Message<String> updateDataSource(DataSource dataSource);

    Message<String> deleteDataSource(String id);

    boolean testConnection(DataSource dataSource);

    void updateStatus(String id, DataSourceStatus status);

    javax.sql.DataSource buildDataSource(DataSource cfg);
}
