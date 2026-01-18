package org.dromara.surpass.persistence.service;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.app.App;
import org.dromara.surpass.entity.app.dto.AppChangeDto;
import org.dromara.surpass.entity.app.dto.AppPageDto;

import java.util.List;

public interface AppService extends IJpaService<App> {
    Message<String> create(AppChangeDto dto);

    Message<String> updateApp(AppChangeDto dto);

    App findByClientId(String clientId);
    
    App findByContextPath(String contextPath);

    String issueToken(App app);

    void revokeToken(String token);

    JpaPageResults<App> fetchPageResults(AppPageDto dto);

    Message<String> deleteApp(List<String> ids);
}
