package org.dromara.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.entity.api.ApiPublishRecord;

import java.util.List;
import java.util.Optional;

public interface ApiPublishService extends IJpaService<ApiPublishRecord> {
    List<ApiPublishRecord> getPublishHistory(String apiId);

    Optional<ApiPublishRecord> getLatestPublishRecord(String apiId);

    void publishVersion(String apiId, String versionId, String operator, String description);

    void offlineVersion(String apiId, String versionId, String operator, String description);

    void submitForReview(String apiId, String versionId);

    void rejectVersion(String apiId, String versionId, String reason);

    void onlineVersion(String apiId, String versionId, String operator, String description);
}
