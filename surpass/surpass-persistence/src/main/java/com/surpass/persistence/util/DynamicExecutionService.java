package com.surpass.persistence.util;

import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.ApiVersion;
import com.surpass.entity.api.DataSource;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.ApiDefinitionService;
import com.surpass.persistence.service.ApiVersionService;
import com.surpass.persistence.service.DataSourceService;
import com.surpass.persistence.service.impl.DataSourceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DynamicExecutionService {
    private static final Logger logger = LoggerFactory.getLogger(DynamicExecutionService.class);

    private final SqlTemplateParser sqlTemplateParser;

    private final SqlExecutor sqlExecutor;

    private final ApiDefinitionService apiDefinitionService;

    private final ApiVersionService apiVersionService;

    private final DataSourceService dataSourceService;

    public Object executeApi(String path, String method, Map<String, Object> params) {
        try {
            // 1. 根据路径和方法查找API定义
            ApiDefinition apiDefinition = apiDefinitionService.findByPathAndMethod(path, method);
            if (Objects.isNull(apiDefinition)) {
                throw new BusinessException(50001, "API不存在");
            }

            // 2. 查找已发布的版本
            ApiVersion apiVersion = apiVersionService.findPublishedVersionByApiId(apiDefinition.getId());
            if (Objects.isNull(apiVersion)) {
                throw new BusinessException(50001, "API未发布");
            }

            // 3. 获取数据源名称
            DataSource dataSource = dataSourceService.get(apiDefinition.getDatasourceId());
            if (Objects.isNull(dataSource)) {
                throw new BusinessException(50001, "数据源不存在");
            }
            String dataSourceName = dataSource.getName();

            // 4. 解析SQL模板
            SqlTemplateParser.ParsedSql parsedSql = sqlTemplateParser.parseSqlTemplate(
                    apiVersion.getSqlTemplate(), params);

            // 5. 构建参数列表
            List<Object> paramValues = new ArrayList<>();
            for (String paramName : parsedSql.getParamNames()) {
                paramValues.add(params.get(paramName));
            }

            // 6. 执行SQL
            String sql = parsedSql.getSql().toUpperCase().trim();

            if (sql.startsWith("SELECT")) {
                // 查询操作
                return sqlExecutor.executeQuery(dataSourceName, parsedSql.getSql(), paramValues);
            } else if (sql.startsWith("INSERT")) {
                // 插入操作
                Long generatedKey = sqlExecutor.executeInsert(dataSourceName, parsedSql.getSql(), paramValues);
                return Map.of("affectedRows", 1, "generatedKey", generatedKey);
            } else if (sql.startsWith("UPDATE") || sql.startsWith("DELETE")) {
                // 更新或删除操作
                int affectedRows = sqlExecutor.executeUpdate(dataSourceName, parsedSql.getSql(), paramValues);
                return Map.of("affectedRows", affectedRows);
            } else {
                throw new BusinessException(50001, "不支持的SQL类型: " + sql);
            }

        } catch (Exception e) {
            logger.error("执行API失败: {} {}", method, path, e);
            throw new BusinessException(50001, "API执行失败: " + e.getMessage());
        }
    }

    public Object executeApiWithPagination(
            String path,
            String method,
            Map<String, Object> params,
            int pageNum,
            int pageSize) {

        try {
            // 1. 根据路径和方法查找API定义
            ApiDefinition apiDefinition = apiDefinitionService.findByPathAndMethod(path, method);
            if (Objects.isNull(apiDefinition)) {
                throw new BusinessException(50001, "API不存在");
            }

            // 2. 查找已发布的版本
            ApiVersion apiVersion = apiVersionService.findPublishedVersionByApiId(apiDefinition.getId());
            if (Objects.isNull(apiVersion)) {
                throw new BusinessException(50001, "API未发布");
            }

            // 3. 获取数据源名称
            // 3. 获取数据源名称
            DataSource dataSource = dataSourceService.get(apiDefinition.getDatasourceId());
            if (Objects.isNull(dataSource)) {
                throw new BusinessException(50001, "数据源不存在");
            }
            String dataSourceName = dataSource.getName();

            // 4. 解析SQL模板
            SqlTemplateParser.ParsedSql parsedSql = sqlTemplateParser.parseSqlTemplate(
                    apiVersion.getSqlTemplate(), params);

            // 5. 构建参数列表
            List<Object> paramValues = new ArrayList<>();
            for (String paramName : parsedSql.getParamNames()) {
                paramValues.add(params.get(paramName));
            }

            // 6. 执行分页查询
            String sql = parsedSql.getSql().toUpperCase().trim();
            if (!sql.startsWith("SELECT")) {
                throw new BusinessException(50001, "分页查询只支持SELECT语句");
            }

            List<Map<String, Object>> data = sqlExecutor.executeQueryWithPagination(
                    dataSourceName, parsedSql.getSql(), paramValues, pageNum, pageSize);

            return Map.of(
                    "data", data,
                    "pageNum", pageNum,
                    "pageSize", pageSize
            );

        } catch (Exception e) {
            logger.error("执行分页API失败: {} {}", method, path, e);
            throw new BusinessException(50001, "API执行失败: " + e.getMessage());
        }
    }
}
