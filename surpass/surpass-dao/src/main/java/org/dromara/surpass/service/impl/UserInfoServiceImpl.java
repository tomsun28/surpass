package org.dromara.surpass.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.crypto.password.PasswordReciprocal;
import org.dromara.surpass.dao.UserInfoDao;
import org.dromara.surpass.pojo.entity.ChangePassword;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.service.PasswordPolicyValidatorService;
import org.dromara.surpass.service.UserInfoService;
import org.dromara.surpass.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/17 9:55
 */

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends JpaServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoDao userInfoDao;

    private final PasswordEncoder passwordEncoder;

    private final PasswordPolicyValidatorService passwordPolicyValidatorService;

    @Override
    public UserInfo findByUsername(String username) {
        return getMapper().findByUsername(username);
    }

    @Override
    @Transactional
    public boolean changePassword(ChangePassword changePassword, boolean passwordPolicy) {
        logger.debug("decipherable old : {}", changePassword.getDecipherable());
        logger.debug("decipherable new : {}", PasswordReciprocal.getInstance().encode(changePassword.getDecipherable()));

        if (passwordPolicy) {
            passwordPolicyValidatorService.validator(changePassword);
        }

        changePassword = passwordEncoder(changePassword);

        if (userInfoDao.changePassword(changePassword) > 0) {
            return true;
        }
        return false;
    }

    public void passwordEncoder(UserInfo userInfo) {
        ChangePassword changePassword = null;
        if (StringUtils.isNotBlank(userInfo.getPassword())) {
            changePassword = new ChangePassword(userInfo);
            passwordEncoder(changePassword);
            userInfo.setPassword(changePassword.getPassword());
            userInfo.setDecipherable(changePassword.getDecipherable());
            userInfo.setPasswordLastSetTime(new Date());
        } else {
            userInfo.setPassword(null);
            userInfo.setDecipherable(null);
        }
    }

    public ChangePassword passwordEncoder(ChangePassword changePassword) {
        //密码不为空，则需要进行加密处理
        if (StringUtils.isNotBlank(changePassword.getPassword())) {
            String password = passwordEncoder.encode(changePassword.getPassword());
            changePassword.setDecipherable(PasswordReciprocal.getInstance().encode(changePassword.getPassword()));
            logger.debug("decipherable : {}", changePassword.getDecipherable());
            changePassword.setPassword(password);
            changePassword.setPasswordLastSetTime(DateUtils.getCurrentDateTimeAsString());

        } else {
            changePassword.setPassword(null);
            changePassword.setDecipherable(null);
        }
        return changePassword;
    }


    @Override
    public String randomPassword() {
        return "";
    }

    @Override
    public boolean updateStatus(UserInfo userInfo) {
        return false;
    }
}
