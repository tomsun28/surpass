package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.config.ConfigPasswordPolicy;
import org.passay.Rule;

import java.util.ArrayList;

public interface ConfigPasswordPolicyService extends IJpaService<ConfigPasswordPolicy> {
    public ConfigPasswordPolicy getPasswordPolicy();

    public ArrayList<Rule> getRuleList(ConfigPasswordPolicy passwordPolicy);
}
