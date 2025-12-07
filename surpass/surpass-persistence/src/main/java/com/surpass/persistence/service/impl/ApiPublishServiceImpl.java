package com.surpass.persistence.service.impl;

import com.surpass.entity.api.ApiPublishRecord;
import com.surpass.entity.api.ApiVersion;
import com.surpass.enums.ApiVersionStatus;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.ApiPublishMapper;
import com.surpass.persistence.mapper.ApiVersionMapper;
import com.surpass.persistence.service.ApiPublishService;
import com.surpass.persistence.service.ApiVersionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.OrderBy;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/19 10:00
 */

@Service
@RequiredArgsConstructor
public class ApiPublishServiceImpl extends JpaServiceImpl<ApiPublishMapper, ApiPublishRecord> implements ApiPublishService {

    private static final Logger logger = LoggerFactory.getLogger(ApiPublishServiceImpl.class);

    private final ApiPublishMapper apiPublishMapper;

    private final ApiVersionService apiVersionService;
    private final ApiVersionMapper apiVersionMapper;

    @Override
    public List<ApiPublishRecord> getPublishHistory(String apiId) {
        return apiPublishMapper.findByApiIdOrderByPublishTimeDesc(apiId);
    }

    @Override
    public Optional<ApiPublishRecord> getLatestPublishRecord(String apiId) {
        ApiVersion apiVersion = findByApiIdAndStatus(apiId);

        if (Objects.nonNull(apiVersion)) {
            LambdaQuery<ApiPublishRecord> wrapperPublish = new LambdaQuery<>();
            wrapperPublish.eq(ApiPublishRecord::getApiId, apiId);
            wrapperPublish.eq(ApiPublishRecord::getVersionId, apiVersion.getId());
            wrapperPublish.orderBy(ApiPublishRecord::getPublishTime, OrderBy.DESC.getOrder());
            ApiPublishRecord apiPublishRecord = super.query(wrapperPublish).stream().findFirst().orElse(null);
            if(apiPublishRecord != null){
                apiPublishRecord.setApiVersion(apiVersion);
            }
            return Optional.ofNullable(apiPublishRecord);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void publishVersion(String apiId, String versionId, String operator, String description) {
        // 检查版本是否存在且属于该API
        ApiVersion apiVersion = apiVersionService.get(versionId);
        if (Objects.isNull(apiVersion)) {
            throw new BusinessException(50001, "API版本不存在");
        }

        if (!apiVersion.getApiId().equals(apiId)) {
            throw new BusinessException(50001, "版本不属于该API");
        }

        // 检查版本状态是否为待发布
        if (!apiVersion.getStatus().equals(ApiVersionStatus.PENDING.getCode())) {
            throw new BusinessException(50001, "只有待发布状态的版本才能发布");
        }

        // 取消旧版本的发布状态
        cancelOldPublishedVersion(apiId);

        // 更新版本状态为已发布
        apiVersion.setStatus(ApiVersionStatus.PUBLISHED.getCode());
        apiVersionService.update(apiVersion);

        // 创建发布记录
        ApiPublishRecord publishRecord = new ApiPublishRecord();
        publishRecord.setApiId(apiId);
        publishRecord.setVersionId(versionId);
        publishRecord.setOperator(operator);
        publishRecord.setDescription(StringUtils.isEmpty(description) ? apiVersion.getDescription() : description);
        publishRecord.setPublishTime(LocalDateTime.now());

        super.insert(publishRecord);

        logger.info("API {} 版本 {} 发布成功", apiId, versionId);
    }

    @Override
    @Transactional
    public void offlineVersion(String apiId, String versionId, String operator, String description) {
        // 检查版本是否存在且属于该API
        ApiVersion apiVersion = apiVersionService.get(versionId);
        if (Objects.isNull(apiVersion)) {
            throw new BusinessException(50001, "API版本不存在");
        }

        if (!apiVersion.getApiId().equals(apiId)) {
            throw new BusinessException(50001, "版本不属于该API");
        }

        // 检查版本状态是否为待发布
        if (!apiVersion.getStatus().equals(ApiVersionStatus.PUBLISHED.getCode())) {
            throw new BusinessException(50001, "只有已发布状态的版本才能下线");
        }

        // 更新版本状态为下线
        apiVersion.setStatus(ApiVersionStatus.OFFLINE.getCode());
        apiVersionService.update(apiVersion);

        logger.info("API {} 版本 {} 下线成功", apiId, versionId);
    }

    @Override
    public void submitForReview(String apiId, String versionId) {
        // 检查版本是否存在且属于该API
        ApiVersion apiVersion = apiVersionService.get(versionId);

        if (Objects.isNull(apiVersion)) {
            throw new BusinessException(50001, "API版本不存在");
        }

        if (!apiVersion.getApiId().equals(apiId)) {
            throw new BusinessException(50001, "版本不属于该API");
        }
        // 检查版本状态是否为草稿
        if (!apiVersion.getStatus().equals(ApiVersionStatus.DRAFT.getCode())) {
            throw new BusinessException(50001, "只有草稿状态的版本才能提交审核");
        }

        // 更新版本状态为待发布
        apiVersion.setStatus(ApiVersionStatus.PENDING.getCode());
        apiVersionService.update(apiVersion);

        logger.info("API {} 版本 {} 提交审核成功", apiId, versionId);
    }

    @Override
    public void rejectVersion(String apiId, String versionId, String reason) {
        // 检查版本是否存在且属于该API
        ApiVersion apiVersion = apiVersionService.get(versionId);

        if (Objects.isNull(apiVersion)) {
            throw new BusinessException(50001, "API版本不存在");
        }

        if (!apiVersion.getApiId().equals(apiId)) {
            throw new BusinessException(50001, "版本不属于该API");
        }
        // 检查版本状态是否为草稿
        if (!apiVersion.getStatus().equals(ApiVersionStatus.PENDING.getCode())) {
            throw new BusinessException(50001, "只有待发布状态的版本才能被驳回");
        }


        // 更新版本状态为草稿
        apiVersion.setStatus(ApiVersionStatus.DRAFT.getCode());
        apiVersionService.update(apiVersion);

        logger.info("API {} 版本 {} 被驳回，原因：{}", apiId, versionId, reason);
    }

    @Override
    public void onlineVersion(String apiId, String versionId, String operator, String description) {
        // 检查版本是否存在且属于该API
        ApiVersion apiVersion = apiVersionService.get(versionId);

        if (!apiVersion.getApiId().equals(apiId)) {
            throw new RuntimeException("版本不属于该API");
        }

        // 检查版本状态是否为下线
        if (!apiVersion.getStatus().equals(ApiVersionStatus.OFFLINE.getCode())) {
            throw new RuntimeException("只有下线状态的版本才能重新上线");
        }

        // 取消旧版本的发布状态
        cancelOldPublishedVersion(apiId);

        // 更新版本状态为已发布
        apiVersion.setStatus(ApiVersionStatus.PUBLISHED.getCode());
        apiVersionService.update(apiVersion);

        // 创建发布记录
        ApiPublishRecord publishRecord = new ApiPublishRecord();
        publishRecord.setApiId(apiId);
        publishRecord.setVersionId(versionId);
        publishRecord.setOperator(operator);
        publishRecord.setDescription(description != null ? description : "重新上线");
        publishRecord.setPublishTime(LocalDateTime.now());

        super.insert(publishRecord);

        logger.info("API {} 版本 {} 重新上线成功", apiId, versionId);
    }

    private void cancelOldPublishedVersion(String apiId) {
        // 查找当前已发布的版本
        ApiVersion oldVersion = findByApiIdAndStatus(apiId);

        if (Objects.nonNull(oldVersion)) {
            oldVersion.setStatus(ApiVersionStatus.OFFLINE.getCode());
            apiVersionService.update(oldVersion);

            logger.info("API {} 旧版本 {} 已下线", apiId, oldVersion.getId());
        }
    }

    /**
     * @Description: 查找当前已发布的版本
     * @Param: [apiId]
     * @return: com.surpass.entity.api.ApiVersion
     * @Author: xZen
     * @Date: 2025/11/19 16:08
     */
    private ApiVersion findByApiIdAndStatus(String apiId) {
        // 先获取已发布的版本
        LambdaQuery<ApiVersion> wrapper = new LambdaQuery<>();
        wrapper.eq(ApiVersion::getApiId, apiId);
        wrapper.eq(ApiVersion::getStatus, ApiVersionStatus.PUBLISHED.getCode());
        wrapper.orderBy(ApiVersion::getCreatedDate, OrderBy.DESC.getOrder());
        return apiVersionService.query(wrapper).stream().findFirst().orElse(null);
    }
}
