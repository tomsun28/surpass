package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.Institutions;
import org.dromara.surpass.pojo.entity.config.ConfigLoginPolicy;
import org.dromara.surpass.pojo.entity.history.HistoryLogin;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.pojo.entity.permissions.Resources;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface LoginService extends IJpaService<UserInfo> {

    void updateLastLogin(UserInfo userInfo);

    UserInfo findById(String userId);

    UserInfo findByUsername(String loginName);

    List<GrantedAuthority> grantAuthority(UserInfo userInfo) ;

    boolean applyLoginPolicy(UserInfo userInfo);

    void updateLockUser(UserInfo userInfo);

    void updateUnlockUser(UserInfo userInfo);

    void updateLoginFailedCount(String userId) ;

    void updateBadPasswordCount(UserInfo userInfo);

    void updateLoginFailedCountReset(UserInfo userInfo) ;

    void coverPassword(UserInfo userInfo, String password);

    ConfigLoginPolicy getConfigLoginPolicy();

    void insertHistory(HistoryLogin historyLogin);
}
