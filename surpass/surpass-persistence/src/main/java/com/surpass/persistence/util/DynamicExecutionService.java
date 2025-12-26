package com.surpass.persistence.util;

import com.surpass.entity.api.ApiVersion;
import com.surpass.entity.api.DataSource;
import com.surpass.entity.app.AppResources;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.ApiVersionService;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.persistence.service.DataSourceService;
import lombok.RequiredArgsConstructor;
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

    private final ApiVersionService apiVersionService;

    private final AppResourcesService appResourcesService;

    private final DataSourceService dataSourceService;

    private final String DEFAULT_PAGE_NUM_KEY = "_pageNum";
    private final String DEFAULT_PAGE_SIZE_KEY = "_pageSize";

    public Object executeApi(String contextPath,String path, String method,  Map<String, Object> params) {
        try {
            // 1. 根据路径和方法查找API定义
            AppResources byPathAndMethod = appResourcesService.findByPathAndMethod(path, method, contextPath);
            if (Objects.isNull(byPathAndMethod)) {
                throw new BusinessException(50001, "API不存在");
            }

            // 2. 查找已发布的版本
            ApiVersion apiVersion = apiVersionService.findPublishedVersionByApiId(byPathAndMethod.getId());
            if (Objects.isNull(apiVersion)) {
                throw new BusinessException(50001, "API未发布");
            }

            // 判断是否分页
            if (apiVersion.getSupportsPaging() != null && apiVersion.getSupportsPaging().equals(1)) {
                int pageNum = Integer.parseInt(params.getOrDefault(DEFAULT_PAGE_NUM_KEY, "1").toString());
                int pageSize = Integer.parseInt(params.getOrDefault(DEFAULT_PAGE_SIZE_KEY, "20").toString());
                return executeApiWithPagination(byPathAndMethod, apiVersion, params, pageNum, pageSize);
            }

            // 3. 获取数据源名称
            DataSource dataSource = dataSourceService.get(byPathAndMethod.getDatasourceId());
            if (Objects.isNull(dataSource)) {
                throw new BusinessException(50001, "数据源不存在");
            }

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
                return sqlExecutor.executeQuery(dataSource, parsedSql.getSql(), paramValues);
            } else if (sql.startsWith("INSERT")) {
                // 插入操作
                Long generatedKey = sqlExecutor.executeInsert(dataSource, parsedSql.getSql(), paramValues);
                return Map.of("affectedRows", 1, "generatedKey", generatedKey);
            } else if (sql.startsWith("UPDATE") || sql.startsWith("DELETE")) {
                // 更新或删除操作
                int affectedRows = sqlExecutor.executeUpdate(dataSource, parsedSql.getSql(), paramValues);
                return Map.of("affectedRows", affectedRows);
            } else {
                throw new BusinessException(50001, "不支持的SQL类型: " + sql);
            }

        } catch (Exception e) {
            logger.error("执行API失败: {} {}", method, path, e);
            throw new BusinessException(50001, "API执行失败: " + e.getMessage());
        }
    }

    public SqlExecutor.PaginatedResult executeApiWithPagination(
            AppResources appResources,
            ApiVersion apiVersion,
            Map<String, Object> params,
            int pageNum,
            int pageSize) {
        try {
            DataSource dataSource = dataSourceService.get(appResources.getDatasourceId());
            if (Objects.isNull(dataSource)) {
                throw new BusinessException(50001, "数据源不存在");
            }
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

            return sqlExecutor.executeQueryWithPagination(
                    dataSource, parsedSql.getSql(), paramValues, pageNum, pageSize);
        } catch (Exception e) {
            logger.error("执行分页API失败: {} {}", appResources.getMethod(), appResources.getPath(), e);
            throw new BusinessException(50001, "API执行失败: " + e.getMessage());
        }
    }
}
