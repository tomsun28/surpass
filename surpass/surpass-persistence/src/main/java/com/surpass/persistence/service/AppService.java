package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface AppService extends IJpaService<App> {
    Message<String> create(App app);

    App findByClientId(String clientId);

    String issueToken(App app);

    void revokeToken(String token);
}
