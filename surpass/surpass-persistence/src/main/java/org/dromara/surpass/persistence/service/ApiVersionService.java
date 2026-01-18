package org.dromara.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.api.ApiVersion;
import org.dromara.surpass.enums.ApiVersionStatus;

import java.util.Map;

public interface ApiVersionService extends IJpaService<ApiVersion> {
    ApiVersion findPublishedVersionByApiId(String apiId);

    Message<ApiVersion> createNewVersion(ApiVersion apiVersion);

    void updateStatus(String id, ApiVersionStatus status);

    boolean updateVersion(ApiVersion apiVersion);

    String generateDiff(String oldVersionId, String newVersionId);

    Map<String, Long> getVersionStatusStatistics(String apiId);
}
