package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.config.ConfigLoginPolicy;

public interface ConfigLoginPolicyService extends IJpaService<ConfigLoginPolicy> {
    ConfigLoginPolicy getConfigLoginPolicy();
}
