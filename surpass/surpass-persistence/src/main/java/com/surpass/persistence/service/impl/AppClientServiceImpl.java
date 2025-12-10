package com.surpass.persistence.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import com.surpass.entity.app.AppClient;
import com.surpass.entity.app.dto.AppClientChangeDto;
import com.surpass.entity.app.dto.AppClientPageDto;
import com.surpass.entity.app.dto.AppPageDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.AppClientMapper;
import com.surpass.persistence.service.AppClientService;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/9 17:27
 */

@Service
public class AppClientServiceImpl extends JpaServiceImpl<AppClientMapper, AppClient> implements AppClientService {
    @Override
    public Message<String> create(AppClientChangeDto dto) {
        checkClientName(dto, false);
        AppClient appClient = BeanUtil.copyProperties(dto, AppClient.class);
        appClient.setClientId(UUID.randomUUID().toString().replace("-", ""));
        appClient.setClientSecret(BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt()));

        boolean result = super.insert(appClient);
        return result ? Message.ok("创建成功") : Message.failed("创建失败");
    }

    @Override
    public Message<String> updateApp(AppClientChangeDto dto) {
        checkClientName(dto, true);
        AppClient appClient = BeanUtil.copyProperties(dto, AppClient.class);

        boolean result = super.update(appClient);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    private void checkClientName(AppClientChangeDto dto, boolean isEdit) {
        LambdaQuery<AppClient> wrapper = new LambdaQuery<>();
        wrapper.eq(AppClient::getClientName, dto.getClientName());
        if (isEdit) {
            wrapper.notEq(AppClient::getId, dto.getId());
        }
        List<AppClient> query = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(query)) {
            throw new BusinessException(50001, "该客户端名称已被使用，请重新输入");
        }
    }

    @Override
    public JpaPageResults<AppClient> fetchPageResults(AppClientPageDto dto) {
        AppClient appClient = BeanUtil.copyProperties(dto, AppClient.class);
        appClient.setDeleted("n");
        dto.build();
        return super.fetch(dto, appClient);
    }
}
