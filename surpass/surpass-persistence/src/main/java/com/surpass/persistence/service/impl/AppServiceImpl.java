package com.surpass.persistence.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.AppMapper;
import com.surpass.persistence.service.AppService;
import com.surpass.security.TokenStore;
import lombok.RequiredArgsConstructor;
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
    public Message<String> create(App app) {
        app.setClientId(UUID.randomUUID().toString().replace("-", ""));
        app.setClientSecret(BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt()));

        app.setStatus(0);

        boolean result = super.insert(app);
        return result ? Message.ok("创建成功") : Message.failed("创建失败");
    }

    @Override
    public App findByClientId(String clientId) {
        LambdaQuery<App> wrapper = new LambdaQuery<>();
        wrapper.eq(App::getClientId, clientId);
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
        int ttl = app.getTtlSeconds() == null ? 7200 : app.getTtlSeconds();
        tokenStore.storeToken(token, app.getClientId(), Duration.ofSeconds(ttl));

        logger.info("Issued app token for clientId={}, ttl={}s", app.getClientId(), ttl);
        return token;
    }

    /** token 吊销 */
    @Override
    public void revokeToken(String token) {
        tokenStore.revoke(token);
        logger.info("Revoked app token {}", token);
    }
}
