package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.DataSource;
import com.surpass.enums.DataSourceStatus;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.DataSourceMapper;
import com.surpass.persistence.service.DataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/19 10:02
 */

@Service
public class DataSourceServiceImpl extends JpaServiceImpl<DataSourceMapper, DataSource> implements DataSourceService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    @Override
    public Message<String> saveDataSource(DataSource dataSource) {
        checkDuplicateName(dataSource);

        return super.insert(dataSource) ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<String> updateDataSource(DataSource dataSource) {
        checkDuplicateName(dataSource);

        return super.update(dataSource) ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public boolean testConnection(DataSource dataSource) {
        try {
            // 解密密码进行测试 - 暂时禁用
            // String decryptedPassword = decryptPassword(dataSource.getPassword());
            String decryptedPassword = dataSource.getPassword();

            Class.forName(dataSource.getDriverClassName());
            try (Connection connection = DriverManager.getConnection(
                    dataSource.getUrl(),
                    dataSource.getUsername(),
                    decryptedPassword)) {

                String testSql = StringUtils.isNotBlank(dataSource.getTestSql()) ?
                        dataSource.getTestSql() : "SELECT 1";

                try (var statement = connection.createStatement();
                     var resultSet = statement.executeQuery(testSql)) {
                    return resultSet.next();
                }
            }
        } catch (Exception e) {
            logger.error("测试数据源连接失败: {}", dataSource.getName(), e);
            return false;
        }
    }

    @Override
    public void updateStatus(String id, DataSourceStatus status) {
        DataSource dataSource = super.get(id);
        if (Objects.isNull(dataSource)) {
            throw new BusinessException(50001, "数据源不存在");
        }
        dataSource.setStatus(status.getCode());
        super.update(dataSource);
    }

    private void checkDuplicateName(DataSource dataSource) {
        LambdaQuery<DataSource> wrapper = new LambdaQuery<>();
        wrapper.eq(DataSource::getName, dataSource.getName());
        if (StringUtils.isNotBlank(dataSource.getId())) {
            wrapper.notEq(DataSource::getId ,dataSource.getId());
        }

        if (super.count(wrapper) > 0) {
            throw new BusinessException(50001, "数据源名称已存在");
        }
    }
}
