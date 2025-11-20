package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiVersion;
import com.surpass.enums.ApiVersionStatus;
import org.dromara.mybatis.jpa.service.IJpaService;

import java.util.Map;

public interface ApiVersionService extends IJpaService<ApiVersion> {
    ApiVersion findPublishedVersionByApiId(String apiId);

    Message<ApiVersion> createNewVersion(ApiVersion apiVersion);

    void updateStatus(String id, ApiVersionStatus status);

    boolean updateVersion(ApiVersion apiVersion);

    String generateDiff(String oldVersionId, String newVersionId);

    Map<String, Long> getVersionStatusStatistics(String apiId);
}
