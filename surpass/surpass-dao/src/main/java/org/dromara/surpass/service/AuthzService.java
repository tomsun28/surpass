package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.pojo.entity.permissions.Roles;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthzService extends IJpaService<UserInfo> {
    public List<Roles> queryRoles(UserInfo userInfo);

    public List<Roles> queryRolesByMembers(UserInfo userInfo);

    public List<GrantedAuthority> grantAuthority(UserInfo userInfo);
}
