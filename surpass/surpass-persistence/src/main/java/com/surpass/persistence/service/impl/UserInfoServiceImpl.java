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




package com.surpass.persistence.service.impl;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.Institutions;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigEmailSenders;
import com.surpass.entity.idm.dto.RegisterUserDto;
import com.surpass.entity.idm.dto.UserInfoPageDto;
import com.surpass.entity.permissions.RoleMember;
import com.surpass.entity.permissions.Roles;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.ConfigEmailSendersService;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.configuration.ApplicationConfig;
import com.surpass.configuration.EmailConfig;
import com.surpass.constants.ConstsBoolean;
import com.surpass.constants.ConstsStatus;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.entity.ChangePassword;
import com.surpass.entity.idm.UserInfo;
import com.surpass.enums.UsersBusinessCode;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.PasswordPolicyValidatorService;
import com.surpass.persistence.service.UserInfoService;
import com.surpass.util.DateUtils;
import jakarta.mail.internet.MimeMessage;

import org.springframework.transaction.annotation.Transactional;


/**
 * @author Crystal.Sea
 */
@Repository
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordPolicyValidatorService passwordPolicyValidatorService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    InstitutionsMapper institutionsMapper;

    @Autowired
    IdentifierGenerator identifierGenerator;

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
    public Message<Page<UserInfo>> fetchPageResults(UserInfoPageDto dto) {
    	Page<UserInfo> page = userInfoMapper.fetchPageResults(dto.build(), dto);
    	for(UserInfo user : page.getRecords()) {
    		user.clearSensitive();
    	}
        return Message.ok(page);
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

        return super.save(userInfo);
    }

    /**
     * @Description: 校验登录账号是否重复
     * @Param: [username]
     * @return: void
     */
    public void checkUsernameDuplicate(String username, String id) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUsername, username);
        if (StringUtils.isNotEmpty(id)) {
            wrapper.ne(UserInfo::getId, id);
        }
        List<UserInfo> query = super.list(wrapper);
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
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getMobile, mobile);
        wrapper.notIn(UserInfo::getId, id);
        List<UserInfo> query = super.list(wrapper);
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

        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<UserInfo>();
        wrapper.eq(UserInfo::getEmail, email);
        if (StringUtils.isNotEmpty(id)) {
            wrapper.ne(UserInfo::getId, id);
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

        userInfo.setPassword(super.getById(id).getPassword());
        userInfo.setDecipherable(super.getById(id).getDecipherable());

        return super.updateById(userInfo);
    }

    public boolean delete(UserInfo userInfo) {
        return super.removeById(userInfo.getId());
    }

	@Override
    public UserInfo findByUsername(String username) {
        return getMapper().findByUsername(username);
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

    @Override
    @Transactional
    public Message<String> handleRegister(RegisterUserDto registerUserDto) {
        String username = registerUserDto.getUsername();
        String password = registerUserDto.getPassword();
        String confirmPassword = registerUserDto.getConfirmPassword();
        String instName = registerUserDto.getInstName();
        String email = registerUserDto.getEmail();

        //校验用户邮箱是否已经被使用
        checkEmailDuplicate(email, null);

        if (!password.equals(confirmPassword)) {
            throw new BusinessException(50001,
                    "密码和确认密码不一致，请检查");
        }

        //校验登录账号
        checkUsernameDuplicate(username, null);
        //密码规则验证
        passwordPolicyValidatorService.validator(new ChangePassword(username, password));

        UserInfo userInfo = new UserInfo();
        String id = identifierGenerator.nextId(userInfo).toString();
        userInfo.setDisplayName(registerUserDto.getInstName());
        userInfo.setNickName(registerUserDto.getInstName());
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setId(id);
        //活动用户
        userInfo.setStatus(1);
        userInfo.setEmail(email);
        userInfo.setMobile(registerUserDto.getUsername());

        userInfo.setEmailVerified(ConstsBoolean.TRUE);
        passwordEncoder(userInfo);

        //添加机构信息
        Institutions institutions = new Institutions();
        institutions.setId(identifierGenerator.nextId(institutions).toString());
        institutions.setFullName(instName);
        institutions.setInstName(instName);
        institutions.setInstType(registerUserDto.getInstType());
        //设置联系人
        institutions.setContact(username);
        institutions.setEmail(email);
        institutions.setOwnerId(userInfo.getId());
        institutions.setStatus(ConstsStatus.ACTIVE);

        //赋予注册用户默认权限
        Roles roles = rolesMapper.selectOne(Wrappers.<Roles>lambdaQuery()
                .eq(Roles::getRoleCode, "2000"));
        String rolesId = roles.getId();
        RoleMember roleMember = new RoleMember();
        roleMember.setRoleId(rolesId);
        roleMember.setMemberId(userInfo.getId());
        roleMember.setType("USER");
        userInfo.setInstId(institutions.getId());
        boolean result = super.save(userInfo);

        if (result) {
            institutionsMapper.insert(institutions);
            roleMemberMapper.insert(roleMember);
            //注册后给管理员发邮件
            if(applicationConfig.isRegisterMailToSupport()) {
            	sendRegisterMailToSupport(registerUserDto);
            }
        }

        return result ? Message.ok("注册成功") : Message.failed("注册失败");
    }

   private void sendRegisterMailToSupport(RegisterUserDto registerUserDto) {
	   try {
		   LambdaQueryWrapper<ConfigEmailSenders> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(ConfigEmailSenders::getStatus, ConstsStatus.ACTIVE)
					.orderByDesc(ConfigEmailSenders::getModifiedDate);
			ConfigEmailSenders configEmailSender = configEmailSendersService.getOne(queryWrapper, false);

			String credentials = PasswordReciprocal.getInstance().decoder(configEmailSender.getCredentials());
			EmailConfig emailConfig = new EmailConfig(
									configEmailSender.getAccount(),
									credentials,
									configEmailSender.getSmtpHost(),
									configEmailSender.getPort(),
									ConstsBoolean.isTrue(configEmailSender.getSslSwitch()),
									configEmailSender.getSender());
		  //Sender
	       JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
	       javaMailSender.setUsername(emailConfig.getUsername());
	       javaMailSender.setPassword(emailConfig.getPassword());
	       Properties properties = new Properties();
	       properties.put("mail.smtp.auth", "true");
	       properties.put("mail.smtp.ssl.enable", "true");
	       javaMailSender.setJavaMailProperties(properties);
	       javaMailSender.setHost(emailConfig.getSmtpHost());
	       javaMailSender.setPort(emailConfig.getPort());

	       // 创建 MimeMessage
	       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

	       // 设置邮件信息
	       // 发件人地址（建议用 setUsername 的邮箱）
	       helper.setTo("support@surpass.com");
	       helper.setFrom(emailConfig.getUsername());
	       helper.setSubject("用户 "+registerUserDto.getUsername() +" "+registerUserDto.getInstName()+" 注册了Surpass云服务");
	       String instType = registerUserDto.getInstType().equals(0) ? "机构" :"个人";
	       // 设置 HTML 格式内容
	       String content= String.format(
	    		   """
	       		   	<html>
		       			<style>
						    table {
						        width: 400px;
						        margin: 0 auto;
						        border: 1px solid #000000;
						        border-collapse: collapse;
						    }
						    
						    th{
						        border: 1px solid #000000;
						        text-align: center;
						    }
						    
						    td {
						        border: 1px solid #000000;
						        text-align: left;
						    }
						</style>
		       			用户注册信息:<br>
		       			<table>
		       				<tr><th>条目</th><th>信息</th></tr>
			       			<tr><td>机构</td><td>%s</td></tr>
			       			<tr><td>机构类型</td><td>%s</td></tr>
			       			<tr><td>用户名</td><td>%s</td></tr>
			       			<tr><td>邮箱</td><td>%s</td></tr>
			       			<tr><td>注册时间</td><td>%s</td></tr>
			       		</table>
		       		</html>
		       		""",
       				registerUserDto.getInstName(),
       				instType,
       				registerUserDto.getUsername(),
       				registerUserDto.getEmail(),
       				DateUtils.getCurrentDateTimeAsString()
	       		);

	       // 👈 第二个参数 true 表示启用 HTML
	       helper.setText(content, true);

	       // 发送
	       javaMailSender.send(mimeMessage);

	       logger.debug("注册通知邮件发送成功");
	   }catch(Exception e) {
		   logger.error("注册通知邮件发送失败",e);
	   }
   }
}
