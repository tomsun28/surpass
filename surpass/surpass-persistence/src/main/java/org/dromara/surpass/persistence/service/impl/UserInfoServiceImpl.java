/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */




package org.dromara.surpass.persistence.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.configuration.ApplicationConfig;
import org.dromara.surpass.crypto.password.PasswordReciprocal;
import org.dromara.surpass.entity.ChangePassword;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.idm.UserInfo;
import org.dromara.surpass.entity.idm.dto.UserInfoPageDto;
import org.dromara.surpass.enums.UsersBusinessCode;
import org.dromara.surpass.exception.BusinessException;
import org.dromara.surpass.persistence.mapper.*;
import org.dromara.surpass.persistence.service.ConfigEmailSendersService;
import org.dromara.surpass.persistence.service.PasswordPolicyValidatorService;
import org.dromara.surpass.persistence.service.UserInfoService;
import org.dromara.surpass.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Crystal.Sea
 */
@Service
public class UserInfoServiceImpl extends JpaServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordPolicyValidatorService passwordPolicyValidatorService;

    @Autowired
    UserInfoMapper userInfoMapper;
    
    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    RoleMemberMapper roleMemberMapper;

    @Autowired
    ConfigEmailSendersService configEmailSendersService;

    @Autowired
    ApplicationConfig applicationConfig;

    public UserInfoMapper getMapper() {
        return userInfoMapper;
    }


    @Override
    public Message<JpaPageResults<UserInfo>> fetchPageResults(UserInfoPageDto dto) {
        dto.build();
        JpaPageResults<UserInfo> jpaPageResults = (JpaPageResults<UserInfo>) this.buildPageResults(dto, getMapper().fetchPageResults(dto));
    	for(UserInfo user : jpaPageResults.getRows()) {
    		user.clearSensitive();
    	}
        return Message.ok(jpaPageResults);
    }

    @Override
    @Transactional
    public boolean saveOneUser(UserInfo userInfo) {
        String username = userInfo.getUsername();
        String mobile = userInfo.getMobile();
        String email = userInfo.getEmail();
        String password = userInfo.getPassword();

        //校验登录账号
        checkUsernameDuplicate(username, null);
        //校验手机号码
        checkMobileDuplicate(mobile, null);
        //校验邮箱地址
        checkEmailDuplicate(email, null);
        //密码规则验证
        passwordPolicyValidatorService.validator(new ChangePassword(username, password));

        passwordEncoder(userInfo);

        return super.insert(userInfo);
    }

    /**
     * @Description: 校验登录账号是否重复
     * @Param: [username]
     * @return: void
     */
    public void checkUsernameDuplicate(String username, String id) {
        LambdaQuery<UserInfo> wrapper = new LambdaQuery<>();
        wrapper.eq(UserInfo::getUsername, username);
        if (StringUtils.isNotEmpty(id)) {
            wrapper.notEq(UserInfo::getId, id);
        }
        List<UserInfo> query = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(query)) {
            throw new BusinessException(
                    UsersBusinessCode.USERNAME_USED.getCode(),
                    UsersBusinessCode.USERNAME_USED.getMsg()
            );
        }
    }

    /**
     * @Description: 校验手机号码是否重复
     * @Param: [mobile, id]
     * @return: void
     */
    public void checkMobileDuplicate(String mobile, String id) {
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        LambdaQuery<UserInfo> wrapper = new LambdaQuery<>();
        wrapper.eq(UserInfo::getMobile, mobile);
        wrapper.notEq(UserInfo::getId, id);
        List<UserInfo> query = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(query)) {
            throw new BusinessException(
                    UsersBusinessCode.MOBILE_USED.getCode(),
                    UsersBusinessCode.MOBILE_USED.getMsg()
            );
        }
    }

    /**
     * @Description: 校验邮箱是否重复
     * @Param: [email, id]
     * @return: void
     */
    public void checkEmailDuplicate(String email, String id) {
        if (StringUtils.isBlank(email)) {
            return;
        }

        LambdaQuery<UserInfo> wrapper = new LambdaQuery<>();
        wrapper.eq(UserInfo::getEmail, email);
        if (StringUtils.isNotEmpty(id)) {
            wrapper.notEq(UserInfo::getId, id);
        }
        long count = super.count(wrapper);
        if (count > 0) {
            throw new BusinessException(
                    UsersBusinessCode.EMAIL_USED.getCode(),
                    UsersBusinessCode.EMAIL_USED.getMsg()
            );
        }
    }


    @Override
    @Transactional
    public boolean updateOneUser(UserInfo userInfo) {
        String username = userInfo.getUsername();
        String mobile = userInfo.getMobile();
        String email = userInfo.getEmail();
        String id = userInfo.getId();


        //校验登录账号
        checkUsernameDuplicate(username, id);
        //校验手机号码
        checkMobileDuplicate(mobile, id);
        //校验邮箱地址
        checkEmailDuplicate(email, id);

        userInfo.setPassword(super.get(id).getPassword());
        userInfo.setDecipherable(super.get(id).getDecipherable());

        return super.update(userInfo);
    }

    public boolean delete(UserInfo userInfo) {
        return super.delete(userInfo.getId());
    }

	@Override
    public UserInfo findByUsername(String username) {
        return getMapper().findByUsername(username);
    }

    @Override
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

    /**
     * @Description: 后台密码修改
     * @Param: [changePassword, passwordPolicy]
     * @return: boolean
     */
    @Override
    @Transactional
    public boolean changePassword(ChangePassword changePassword, boolean passwordPolicy) {
        logger.debug("decipherable old : {}", changePassword.getDecipherable());
        logger.debug("decipherable new : {}", PasswordReciprocal.getInstance().encode(changePassword.getDecipherable()));

        if (passwordPolicy) {
            passwordPolicyValidatorService.validator(changePassword);
        }

        changePassword = passwordEncoder(changePassword);

        if (getMapper().changePassword(changePassword) > 0) {
            return true;
        }
        return false;
    }

	@Override
    public String randomPassword() {
        return passwordPolicyValidatorService.generateRandomPassword();
    }

	@Override
    public boolean updateStatus(UserInfo userInfo) {
        return getMapper().updateStatus(userInfo) > 0;
    }

}
