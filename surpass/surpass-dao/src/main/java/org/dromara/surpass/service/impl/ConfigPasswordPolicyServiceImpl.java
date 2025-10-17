package org.dromara.surpass.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.ConfigPasswordPolicyDao;
import org.dromara.surpass.pojo.entity.config.ConfigPasswordPolicy;
import org.dromara.surpass.service.ConfigPasswordPolicyService;
import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/17 10:38
 */

@Service
@RequiredArgsConstructor
public class ConfigPasswordPolicyServiceImpl extends JpaServiceImpl<ConfigPasswordPolicyDao, ConfigPasswordPolicy> implements ConfigPasswordPolicyService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigPasswordPolicyServiceImpl.class);

    private final ConfigPasswordPolicyDao configPasswordPolicyDao;

    @Override
    public ArrayList<Rule> getRuleList(ConfigPasswordPolicy passwordPolicy) {
        ArrayList <Rule> passwordPolicyRuleList;
        logger.debug("query PasswordPolicy : {}" , passwordPolicy);

        //RandomPasswordLength =(MaxLength +MinLength)/2
        passwordPolicy.setRandomPasswordLength(
                Math.round(
                        (
                                passwordPolicy.getMaxLength() +
                                        passwordPolicy.getMinLength()
                        )/2
                )
        );

        passwordPolicyRuleList = new ArrayList<>();
        passwordPolicyRuleList.add(new WhitespaceRule());
        passwordPolicyRuleList.add(new LengthRule(passwordPolicy.getMinLength(), passwordPolicy.getMaxLength()));

        if(passwordPolicy.getUpperCase()>0) {
            passwordPolicyRuleList.add(new CharacterRule(EnglishCharacterData.UpperCase, passwordPolicy.getUpperCase()));
        }

        if(passwordPolicy.getLowerCase()>0) {
            passwordPolicyRuleList.add(new CharacterRule(EnglishCharacterData.LowerCase, passwordPolicy.getLowerCase()));
        }

        if(passwordPolicy.getDigits()>0) {
            passwordPolicyRuleList.add(new CharacterRule(EnglishCharacterData.Digit, passwordPolicy.getDigits()));
        }

        if(passwordPolicy.getSpecialChar()>0) {
            passwordPolicyRuleList.add(new CharacterRule(EnglishCharacterData.Special, passwordPolicy.getSpecialChar()));
        }

        if(passwordPolicy.getUsername()>0) {
            passwordPolicyRuleList.add(new UsernameRule());
        }

        if(passwordPolicy.getOccurances()>0) {
            passwordPolicyRuleList.add(new CharacterOccurrencesRule(passwordPolicy.getOccurances()));
        }

        if(passwordPolicy.getAlphabetical()>0) {
            passwordPolicyRuleList.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, false));
        }

        if(passwordPolicy.getNumerical()>0) {
            passwordPolicyRuleList.add(new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, false));
        }

        if(passwordPolicy.getQwerty()>0) {
            passwordPolicyRuleList.add(new IllegalSequenceRule(EnglishSequenceData.USQwerty, 4, false));
        }

        return passwordPolicyRuleList;
    }

    @Override
    public ConfigPasswordPolicy getPasswordPolicy() {
        LambdaQuery<ConfigPasswordPolicy> lambdaQueryWrapper = new LambdaQuery<>();
        lambdaQueryWrapper.notNull(ConfigPasswordPolicy::getId);
        return super.get(lambdaQueryWrapper);
    }
}
