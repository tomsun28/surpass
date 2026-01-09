package org.maxkey.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.api.ApiVersion;
import org.maxkey.surpass.enums.ApiVersionStatus;

import java.util.Map;

public interface ApiVersionService extends IJpaService<ApiVersion> {
    ApiVersion findPublishedVersionByApiId(String apiId);

    Message<ApiVersion> createNewVersion(ApiVersion apiVersion);

    void updateStatus(String id, ApiVersionStatus status);

    boolean updateVersion(ApiVersion apiVersion);

    String generateDiff(String oldVersionId, String newVersionId);

    Map<String, Long> getVersionStatusStatistics(String apiId);
}
