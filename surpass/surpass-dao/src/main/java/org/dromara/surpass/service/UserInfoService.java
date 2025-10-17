package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.pojo.entity.ChangePassword;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.pojo.entity.idm.dto.UserInfoPageDto;
import org.springframework.data.domain.Page;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/17 9:49
 */
public interface UserInfoService extends IJpaService<UserInfo> {
    UserInfo findByUsername(String username);

    boolean changePassword(ChangePassword changePassword, boolean passwordPolicy);

    String randomPassword() ;

    boolean updateStatus(UserInfo userInfo) ;
}
