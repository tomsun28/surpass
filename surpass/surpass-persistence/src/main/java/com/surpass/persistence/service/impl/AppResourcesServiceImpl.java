package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesChangeDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.AppResourcesMapper;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.persistence.util.ResourceClassify;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/18 11:15
 */

@Service
public class AppResourcesServiceImpl extends JpaServiceImpl<AppResourcesMapper, AppResources> implements AppResourcesService {


    @Override
    public Message<String> create(AppResourcesChangeDto dto) {
        AppResources appResources = BeanUtil.copyProperties(dto, AppResources.class);

        ResourceClassify classify = ResourceClassify.from(dto.getClassify());

        switch (classify) {
            case OPEN_API -> validateOpenApi(appResources);
            case BUTTON   -> validateButton(appResources);
            case MENU     -> validateMenu(appResources);
            case API      -> validateApi(appResources);
        }

        boolean result = super.insert(appResources);
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    private void requireNotBlank(String value, int code, String message) {
        if (StringUtils.isBlank(value)) {
            throw new BusinessException(code, message);
        }
    }

    private void validateOpenApi(AppResources r) {
        requireNotBlank(r.getDatasourceId(), 50001, "请选择OpenApi所属的数据源");
        requireNotBlank(r.getPath(), 50001, "请填写请求地址");
        requireNotBlank(r.getMethod(), 50001, "请填写请求方式");
    }

    private void validateButton(AppResources r) {
        requireNotBlank(r.getActionType(), 50001, "请填写操作类型");
    }

    private void validateMenu(AppResources r) {
        requireNotBlank(r.getPath(), 50001, "请填写请求路径");
    }

    private void validateApi(AppResources r) {
        requireNotBlank(r.getPath(), 50001, "请填写请求地址");
        requireNotBlank(r.getMethod(), 50001, "请填写请求方式");
    }
}
