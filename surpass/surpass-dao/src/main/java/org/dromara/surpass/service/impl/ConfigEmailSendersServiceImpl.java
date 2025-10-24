package org.dromara.surpass.service.impl;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.ConfigEmailSendersDao;
import org.dromara.surpass.pojo.entity.config.ConfigEmailSenders;
import org.dromara.surpass.service.ConfigEmailSendersService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/22 11:41
 */

@Service
public class ConfigEmailSendersServiceImpl extends JpaServiceImpl<ConfigEmailSendersDao, ConfigEmailSenders> implements ConfigEmailSendersService {

}

