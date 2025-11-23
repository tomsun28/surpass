package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.api.DataSource;
import com.surpass.enums.DataSourceStatus;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.DataSourceMapper;
import com.surpass.persistence.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.datasource.DynamicRoutingDataSource;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/19 10:02
 */

@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl extends JpaServiceImpl<DataSourceMapper, DataSource> implements DataSourceService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    @Override
    public Message<String> saveDataSource(DataSource config) {

        // 1. 校验是否重复（你已有）
        checkDuplicateName(config);

        // 2. 插入记录
        boolean inserted = super.insert(config);
        if (!inserted) {
            return Message.failed("新增失败");
        }

        // 3. 运行时动态增加 DataSource
        try {
            addDynamicDataSource(config);
        } catch (Exception e) {
            // 动态数据源添加失败时，可以选择删除插入的记录，保持一致性
            logger.error("动态添加数据源失败: {}", config.getName(), e);
            return Message.failed("新增成功，但创建数据源失败：" + e.getMessage());
        }

        return Message.ok("新增成功");
    }


    @Override
    public Message<String> updateDataSource(DataSource dataSource) {

        // 1. 检查重名（排除自身）
        checkDuplicateName(dataSource);

        // 2. 查询原来的配置
        DataSource oldCfg = super.get(dataSource.getId());
        if (oldCfg == null) {
            return Message.failed("数据源不存在");
        }

        // 默认数据源不能修改名称
        if ("default".equals(oldCfg.getName()) &&
                !oldCfg.getName().equals(dataSource.getName())) {
            return Message.failed("默认数据源名称不允许修改");
        }

        // 3. 先修改数据库记录
        boolean updated = super.update(dataSource);
        if (!updated) {
            return Message.failed("修改数据源失败");
        }

        // 4. 创建新的 DataSource 对象
        javax.sql.DataSource newDs = buildDataSource(dataSource);
        if (newDs == null) {
            return Message.failed("数据源连接失败，请检查配置");
        }

        // 5. 如果名字变了，先移除旧的
        dynamicRoutingDataSource.removeDataSource(oldCfg.getName());

        // 6. 覆盖添加新的数据源
        boolean added = dynamicRoutingDataSource.addDataSource(dataSource.getName(), newDs);
        if (!added) {
            return Message.failed("动态数据源加载失败，已更新数据库但未生效");
        }

        return Message.ok("修改成功");
    }


    @Override
    public Message<String> deleteDataSource(String id) {

        // 1. 根据 id 查询数据源配置
        DataSource cfg = super.get(id);
        if (cfg == null) {
            return Message.failed("数据源不存在");
        }

        // 默认数据源不能删除
        if ("default".equals(cfg.getName())) {
            return Message.failed("默认数据源不可删除");
        }

        // 2. 先从数据库删除记录
        boolean deleted = super.delete(id);
        if (!deleted) {
            return Message.failed("删除数据源记录失败");
        }

        // 3. 从动态路由中移除数据源
        boolean removed = dynamicRoutingDataSource.removeDataSource(cfg.getName());
        if (!removed) {
            return Message.failed("动态数据源移除失败，记录已删除");
        }

        return Message.ok("删除成功");
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
            wrapper.notEq(DataSource::getId, dataSource.getId());
        }

        if (super.count(wrapper) > 0) {
            throw new BusinessException(50001, "数据源名称已存在");
        }
    }


    private void addDynamicDataSource(DataSource cfg) {

        // 1. 使用 Spring Boot 的 DataSourceBuilder 构建数据源
        javax.sql.DataSource ds = org.springframework.boot.jdbc.DataSourceBuilder
                .create()
                .driverClassName(cfg.getDriverClassName())
                .url(cfg.getUrl())
                .username(cfg.getUsername())
                .password(cfg.getPassword())
                .build();

        // 2. 注册动态数据源（立刻生效）
        boolean ok = dynamicRoutingDataSource.addDataSource(cfg.getName(), ds);
        if (!ok) {
            throw new BusinessException(50001, "数据源已存在: " + cfg.getName());
        }

        logger.info("动态添加数据源 [{}] 成功", cfg.getName());
    }

    @Override
    public javax.sql.DataSource buildDataSource(DataSource cfg) {
        // 1. 使用 Spring Boot 的 DataSourceBuilder 构建数据源
        javax.sql.DataSource ds = org.springframework.boot.jdbc.DataSourceBuilder
                .create()
                .driverClassName(cfg.getDriverClassName())
                .url(cfg.getUrl())
                .username(cfg.getUsername())
                .password(cfg.getPassword())
                .build();
        try {
            ds.getConnection().close(); // 测试有效性
        } catch (Exception e) {
            return null;
        }
        return ds;
    }
}
