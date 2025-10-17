package org.dromara.surpass.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.crypto.password.PasswordGen;
import org.dromara.surpass.exception.BusinessException;
import org.dromara.surpass.pojo.entity.ChangePassword;
import org.dromara.surpass.pojo.entity.config.ConfigPasswordPolicy;
import org.dromara.surpass.service.ConfigPasswordPolicyService;
import org.dromara.surpass.service.PasswordPolicyValidatorService;
import org.dromara.surpass.web.WebContext;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/17 10:10
 */

@Service
public class PasswordPolicyValidatorServiceImpl  implements PasswordPolicyValidatorService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordPolicyValidatorServiceImpl.class);

    public static final String PASSWORD_POLICY_VALIDATE_RESULT = "PASSWORD_POLICY_VALIDATE_RESULT_KEY";

    public static final String PASSWORD_VALIDATOR_KEY = "PASSWORD_VALIDATOR_KEY";

    ConfigPasswordPolicy passwordPolicy;

    //Cache PasswordValidator in memory ONE_HOUR
    protected static final Cache<String, PasswordValidator> passwordValidatorStore =
            Caffeine.newBuilder()
                    .expireAfterWrite(60, TimeUnit.MINUTES)
                    .build();

    ConfigPasswordPolicyService configPasswordPolicyService;

    MessageSource messageSource;

    public PasswordPolicyValidatorServiceImpl() {
    }

    public PasswordPolicyValidatorServiceImpl(ConfigPasswordPolicyService configPasswordPolicyService, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.configPasswordPolicyService = configPasswordPolicyService;
        passwordPolicy = configPasswordPolicyService.getPasswordPolicy();

    }

    /**
     * static validator .
     * @param changePassword
     * @return boolean
     */
    public boolean validator(ChangePassword changePassword) {
        String password = changePassword.getPassword();
        String username = changePassword.getUsername();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            logger.debug("username or password  is Empty ");
            return false;
        }
        PasswordValidator passwordValidator = passwordValidatorStore.getIfPresent(PASSWORD_VALIDATOR_KEY);
        if(passwordValidator == null) {
            passwordPolicy = configPasswordPolicyService.getPasswordPolicy();
            passwordValidator = new PasswordValidator(
                    new PasswordPolicyMessageResolver(messageSource),
                    configPasswordPolicyService.getRuleList(passwordPolicy));
            passwordValidatorStore.put(PASSWORD_VALIDATOR_KEY, passwordValidator);
        }

        RuleResult result = passwordValidator.validate(new PasswordData(username,password));

        if (result.isValid()) {
            logger.debug("Password is valid");
            return true;
        } else {
            logger.debug("Invalid password:");
            StringBuilder passwordPolicyMessage = new StringBuilder("");
            for (String msg : passwordValidator.getMessages(result)) {
                passwordPolicyMessage.append(msg).append("<br>");
                logger.debug("Rule Message {}" , msg);
            }
            WebContext.setAttribute(PasswordPolicyValidatorServiceImpl.PASSWORD_POLICY_VALIDATE_RESULT, passwordPolicyMessage);
            throw new BusinessException(400, String.valueOf(passwordPolicyMessage));
        }
    }

    public String generateRandomPassword() {
        PasswordGen passwordGen = new PasswordGen(
                passwordPolicy.getRandomPasswordLength()
        );

        return passwordGen.gen(
                passwordPolicy.getLowerCase(),
                passwordPolicy.getUpperCase(),
                passwordPolicy.getDigits(),
                passwordPolicy.getSpecialChar());
    }

}
