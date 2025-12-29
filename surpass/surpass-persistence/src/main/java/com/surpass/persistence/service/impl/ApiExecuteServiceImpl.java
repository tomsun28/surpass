package com.surpass.persistence.service.impl;

import com.surpass.entity.ApiRequestUri;
import com.surpass.entity.api.ApiVersion;
import com.surpass.entity.api.DataSource;
import com.surpass.entity.app.AppResources;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.ApiVersionService;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.persistence.service.DataSourceService;
import com.surpass.persistence.service.ISqlRepository;
import com.surpass.persistence.service.ApiExecuteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.datasource.DynamicRoutingDataSource;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiExecuteServiceImpl  implements ApiExecuteService{
    private final ISqlRepository sqlRepository;
    
    private final ApiVersionService apiVersionService;

    private final AppResourcesService appResourcesService;

    private final DataSourceService dataSourceService;
    
    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    private final String DEFAULT_PAGE_NUM_KEY = "_pageNum";
    private final String DEFAULT_PAGE_SIZE_KEY = "_pageSize";

    public Object execute(ApiRequestUri apiRequestUri, String method,  Map<String, Object> params) {
        try {
            // 1. 根据路径和方法查找API定义
            AppResources byPathAndMethod = appResourcesService.findByPathAndMethod(apiRequestUri.getResourcePath(), method, apiRequestUri.getContextPath());
            if (Objects.isNull(byPathAndMethod)) {
                throw new BusinessException(50001, "API不存在");
            }

            // 2. 查找已发布的版本
            ApiVersion apiVersion = apiVersionService.findPublishedVersionByApiId(byPathAndMethod.getId());
            if (Objects.isNull(apiVersion)) {
                throw new BusinessException(50001, "API未发布");
            }

            // 3. 切换数据源
            switchDataSource(byPathAndMethod.getDatasourceId());
            
            // 4. 获取SQL
            String sql = apiVersion.getSqlTemplate().trim();
            String sqlUpperCase = sql.toUpperCase();
            
            
            
            // 判断是否分页
            if (sqlUpperCase.startsWith("SELECT") && apiVersion.getSupportsPaging() != null && apiVersion.getSupportsPaging().equals(1)) {
                int pageNum = Integer.parseInt(params.getOrDefault(DEFAULT_PAGE_NUM_KEY, "1").toString());
                int pageSize = Integer.parseInt(params.getOrDefault(DEFAULT_PAGE_SIZE_KEY, "20").toString());
                JpaPage page = new JpaPage(pageNum,pageSize);
                return sqlRepository.fetch(sql, page, params);
            }else if (sqlUpperCase.startsWith("SELECT")) {
                // 查询操作
                return sqlRepository.selectList(sql, params);
            } else if (sqlUpperCase.startsWith("INSERT")) {
                // 插入操作
                int generatedKey = sqlRepository.insert(sql, params);
                return Map.of("affectedRows", 1, "generatedKey", generatedKey);
            } else if (sqlUpperCase.startsWith("UPDATE")) {
                // 更新
                int affectedRows = sqlRepository.update(sql, params);
                return Map.of("affectedRows", affectedRows);
            }else if (sqlUpperCase.startsWith("DELETE")) {
                // 删除操作
                int affectedRows = sqlRepository.delete(sql, params);
                return Map.of("affectedRows", affectedRows);
            } else {
                throw new BusinessException(50001, "不支持的SQL类型: " + sql);
            }

        } catch (Exception e) {
            log.error("执行API失败: {} {}", method, apiRequestUri.getRequestPath(), e);
            throw new BusinessException(50001, "API执行失败: " + e.getMessage());
        }
    }
    
    private void switchDataSource(String dataSourceId) {
        DataSource dataSource = dataSourceService.get(dataSourceId);
        if (Objects.isNull(dataSource)) {
            throw new BusinessException(50001, "数据源不存在");
        }
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
