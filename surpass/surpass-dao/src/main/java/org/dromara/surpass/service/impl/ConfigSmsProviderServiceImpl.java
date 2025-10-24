package org.dromara.surpass.service.impl;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.ConfigSmsProviderDao;
import org.dromara.surpass.pojo.entity.config.ConfigSmsProvider;
import org.dromara.surpass.service.ConfigSmsProviderService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/22 10:33
 */

@Service
public class ConfigSmsProviderServiceImpl extends JpaServiceImpl<ConfigSmsProviderDao, ConfigSmsProvider> implements ConfigSmsProviderService {

}
