package org.dromara.surpass.service.impl;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.constants.ConstsRoles;
import org.dromara.surpass.dao.AuthzDao;
import org.dromara.surpass.dao.LoginDao;
import org.dromara.surpass.pojo.entity.dto.QueryGroupMembersDto;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.pojo.entity.permissions.Roles;
import org.dromara.surpass.service.AuthzService;
import org.dromara.surpass.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/16 16:20
 */

@Service
public class AuthzServiceImpl extends JpaServiceImpl<AuthzDao, UserInfo> implements AuthzService {
    private static final Logger logger = LoggerFactory.getLogger(AuthzServiceImpl.class);

    @Autowired
    AuthzDao authzDao;

    @Override
    public List<Roles> queryRoles(UserInfo userInfo){
        // query groups for user
        QueryGroupMembersDto groupMembersDto = new QueryGroupMembersDto();
        groupMembersDto.add(userInfo.getId());
        List<Roles> listGroup = authzDao.queryRolesByMembers(groupMembersDto);
        logger.debug("listGroup : {}" , listGroup);
        return listGroup;
    }

    @Override
    public List<Roles> queryRolesByMembers(UserInfo userInfo){
        // query groups for user
        QueryGroupMembersDto groupMembersDto = new QueryGroupMembersDto();
        groupMembersDto.add(userInfo.getId());
        List<Roles> listGroup = authzDao.queryRolesByMembers(groupMembersDto);
        logger.debug("listGroup : {}" , listGroup);
        return listGroup;
    }

    /**
     * grant Authority by userinfo
     *
     * @param userInfo
     * @return ArrayList<GrantedAuthority>
     */
    @Override
    public List<GrantedAuthority> grantAuthority(UserInfo userInfo) {
        List<Roles> listGroup = queryRoles(userInfo);
        //set default groups
        ArrayList<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(ConstsRoles.ROLE_USER);
        grantedAuthority.add(ConstsRoles.ROLE_ALL_USER);
        grantedAuthority.add(ConstsRoles.ROLE_GENERAL_USER);
        for (Roles group : listGroup) {
            grantedAuthority.add(new SimpleGrantedAuthority(group.getId()));
            //Group Code和id不同的情况
            if(!grantedAuthority.contains(new SimpleGrantedAuthority(group.getRoleCode()))) {
                grantedAuthority.add(new SimpleGrantedAuthority(group.getRoleCode()));
            }
            //判断角色类型
            if(group.getCategory().equals(ConstsRoles.Category.SUPERVISOR)) {
                grantedAuthority.add(ConstsRoles.ROLE_SUPERVISOR);
            }else if(group.getCategory().equals(ConstsRoles.Category.ADMINISTRATOR)) {
                grantedAuthority.add(ConstsRoles.ROLE_ADMINISTRATOR);
            }else if(group.getCategory().equals(ConstsRoles.Category.MANAGER)) {
                grantedAuthority.add(ConstsRoles.ROLE_MANAGER);
            }
        }
        logger.debug("Authority : {}" , grantedAuthority);
        return grantedAuthority;
    }

    public UserInfo findUserById(String userId) {
        return authzDao.findUserById(userId);
    }

    public List<Roles> queryGroupsByMembers(QueryGroupMembersDto dto) {
        return authzDao.queryRolesByMembers(dto);
    }

}
