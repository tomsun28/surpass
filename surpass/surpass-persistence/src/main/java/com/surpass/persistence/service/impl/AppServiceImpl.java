package com.surpass.persistence.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import com.surpass.entity.app.dto.AppChangeDto;
import com.surpass.entity.app.dto.AppPageDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.AppMapper;
import com.surpass.persistence.service.AppService;
import com.surpass.security.TokenStore;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/24 15:48
 */

@Service
@RequiredArgsConstructor
public class AppServiceImpl extends JpaServiceImpl<AppMapper, App> implements AppService {
    private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

    private final AppMapper appMapper;

    private final TokenStore tokenStore;

    @Override
    public Message<String> create(AppChangeDto dto) {
        checkAppCode(dto, false);
        App app = BeanUtil.copyProperties(dto, App.class);

        boolean result = super.insert(app);
        return result ? Message.ok("创建成功") : Message.failed("创建失败");
    }

    @Override
    public Message<String> updateApp(AppChangeDto dto) {
        checkAppCode(dto, true);
        App app = BeanUtil.copyProperties(dto, App.class);

        boolean result = super.update(app);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public App findByClientId(String clientId) {
        LambdaQuery<App> wrapper = new LambdaQuery<>();
//        wrapper.eq(App::getClientId, clientId);
        List<App> query = super.query(wrapper);
        if (query.isEmpty()) {
            throw new BusinessException(50001, "应用不存在");
        }

        return query.get(0);
    }

    /** 颁发 token 给第三方 app */
    @Override
    public String issueToken(App app) {
        if (app == null) {
            throw new BusinessException(50001, "应用未找到");
        }

        // 生成 accessToken
        String token = UUID.fastUUID().toString();
//        int ttl = app.getTtlSeconds() == null ? 7200 : app.getTtlSeconds();
//        tokenStore.storeToken(token, app.getClientId(), Duration.ofSeconds(ttl));
//
//        logger.info("Issued app token for clientId={}, ttl={}s", app.getClientId(), ttl);
        return token;
    }

    /** token 吊销 */
    @Override
    public void revokeToken(String token) {
        tokenStore.revoke(token);
        logger.info("Revoked app token {}", token);
    }

    @Override
    public JpaPageResults<App> fetchPageResults(AppPageDto dto) {
        App app = BeanUtil.copyProperties(dto, App.class);
        app.setDeleted("n");
        dto.build();
        return super.fetch(dto, app);
    }

    private void checkAppCode(AppChangeDto dto, boolean isEdit) {
        LambdaQuery<App> wrapper = new LambdaQuery<>();
        wrapper.eq(App::getAppCode, dto.getAppCode());
        if (isEdit) {
            wrapper.notEq(App::getId, dto.getId());
        }
        List<App> query = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(query)) {
            throw new BusinessException(50001, "该应用编码已被使用，请重新输入");
        }
    }
}
