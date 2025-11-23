package com.surpass.persistence.util;

import com.surpass.entity.Message;
import com.surpass.entity.api.DataSource;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.datasource.DynamicRoutingDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqlExecutor {

    private final JdbcTemplate jdbcTemplate;
    private final DynamicRoutingDataSource dynamicRoutingDataSource;
    private final DataSourceService dataSourceService;

    public List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, List<Object> params) {
        try {
            // 动态切换到指定数据源
            switchDataSource(dataSource);

            log.debug("执行查询SQL: {}, 参数: {}", sql, params);

            if (params == null || params.isEmpty()) {
                return jdbcTemplate.queryForList(sql);
            } else {
                return jdbcTemplate.queryForList(sql, params.toArray());
            }
        } catch (Exception e) {
            log.error("执行SQL查询失败: {}", sql, e);
            throw new BusinessException(50001, "SQL执行失败: " + e.getMessage());
        } finally {
            DataSourceSwitch.change(DataSourceSwitch.switchToDefault());
        }
    }

    public int executeUpdate(DataSource dataSource, String sql, List<Object> params) {
        try {
            // 动态切换到指定数据源
            switchDataSource(dataSource);

            log.debug("执行更新SQL: {}, 参数: {}", sql, params);

            if (params == null || params.isEmpty()) {
                return jdbcTemplate.update(sql);
            } else {
                return jdbcTemplate.update(sql, params.toArray());
            }
        } catch (Exception e) {
            log.error("执行SQL更新失败: {}", sql, e);
            throw new BusinessException(50001, "SQL执行失败: " + e.getMessage());
        } finally {
            DataSourceSwitch.change(DataSourceSwitch.switchToDefault());
        }
    }

    public Long executeInsert(DataSource dataSource, String sql, List<Object> params) {
        try {
            // 动态切换到指定数据源
            switchDataSource(dataSource);

            log.debug("执行插入SQL: {}, 参数: {}", sql, params);

            KeyHolder keyHolder = new GeneratedKeyHolder();

            if (params == null || params.isEmpty()) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    return ps;
                }, keyHolder);
            } else {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    for (int i = 0; i < params.size(); i++) {
                        ps.setObject(i + 1, params.get(i));
                    }
                    return ps;
                }, keyHolder);
            }

            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            }
            return null;
        } catch (Exception e) {
            log.error("执行SQL插入失败: {}", sql, e);
            throw new BusinessException(50001, "SQL执行失败: " + e.getMessage());
        } finally {
            DataSourceSwitch.change(DataSourceSwitch.switchToDefault());
        }
    }

    public List<Map<String, Object>> executeQueryWithPagination(
            DataSource dataSource,
            String sql,
            List<Object> params,
            int pageNum,
            int pageSize) {

        try {
            // 动态切换到指定数据源
            switchDataSource(dataSource);

            // 构建分页SQL
            String countSql = "SELECT COUNT(*) FROM (" + sql + ") t";
            String paginationSql = sql + " LIMIT " + (pageNum - 1) * pageSize + ", " + pageSize;

            log.debug("执行分页查询SQL: {}, 参数: {}, 页码: {}, 页大小: {}",
                    paginationSql, params, pageNum, pageSize);

            // 执行查询
            List<Map<String, Object>> data;
            if (params == null || params.isEmpty()) {
                data = jdbcTemplate.queryForList(paginationSql);
            } else {
                data = jdbcTemplate.queryForList(paginationSql, params.toArray());
            }

            return data;
        } catch (Exception e) {
            log.error("执行分页SQL查询失败: {}", sql, e);
            throw new BusinessException(50001, "SQL执行失败: " + e.getMessage());
        } finally {
            DataSourceSwitch.change(DataSourceSwitch.switchToDefault());
        }
    }

    private void switchDataSource(DataSource dataSource) {
        String dataSourceName = dataSource.getName();
        try {
            DataSourceSwitch.change(dataSourceName);
        } catch (Exception e) {
            javax.sql.DataSource newDs = dataSourceService.buildDataSource(dataSource);
            boolean added = dynamicRoutingDataSource.addDataSource(dataSource.getName(), newDs);
            if (!added) {
                throw new BusinessException(50001, e.getMessage());
            }
            DataSourceSwitch.change(dataSourceName);
        }
        log.debug("切换到数据源: {}", dataSourceName);
    }
}
