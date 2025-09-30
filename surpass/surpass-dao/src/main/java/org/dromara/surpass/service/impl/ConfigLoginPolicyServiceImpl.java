package org.dromara.surpass.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.ConfigLoginPolicyDao;
import org.dromara.surpass.pojo.entity.config.ConfigLoginPolicy;
import org.dromara.surpass.service.ConfigLoginPolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/30 15:35
 */

@Service
@RequiredArgsConstructor
public class ConfigLoginPolicyServiceImpl extends JpaServiceImpl<ConfigLoginPolicyDao, ConfigLoginPolicy> implements ConfigLoginPolicyService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoginPolicyServiceImpl.class);

    static final String CONFIG_LOGIN_POLICY_KEY = "CONFIG_LOGIN_POLICY_KEY";


    private final ConfigLoginPolicyDao configLoginPolicyDao;

    //Cache ConfigLoginPolicy in memory ONE_HOUR
    static final Cache<String,  ConfigLoginPolicy> configLoginPolicyStore =
            Caffeine.newBuilder()
                    .expireAfterWrite(60, TimeUnit.MINUTES)
                    .build();

    @Override
    public ConfigLoginPolicy getConfigLoginPolicy() {
        ConfigLoginPolicy configLoginPolicy = configLoginPolicyStore.getIfPresent(CONFIG_LOGIN_POLICY_KEY);
        if (configLoginPolicy == null) {
            LambdaQuery<ConfigLoginPolicy> wrapper = new LambdaQuery<>();
            wrapper.notNull(ConfigLoginPolicy::getId);
            configLoginPolicy = super.get(wrapper);
            configLoginPolicyStore.put(CONFIG_LOGIN_POLICY_KEY,configLoginPolicy);
            logger.debug("get ConfigLoginPolicy : {}" , configLoginPolicy);
        }
        return configLoginPolicy;
    }

}
