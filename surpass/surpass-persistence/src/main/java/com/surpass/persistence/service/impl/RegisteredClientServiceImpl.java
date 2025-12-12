package com.surpass.persistence.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.entity.dto.RegisteredClientChangeDto;
import com.surpass.entity.dto.RegisteredClientPageDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.RegisteredClientMapper;
import com.surpass.persistence.service.RegisteredClientService;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/9 17:27
 */

@Service
public class RegisteredClientServiceImpl extends JpaServiceImpl<RegisteredClientMapper, RegisteredClient> implements RegisteredClientService {
    @Override
    public Message<String> create(RegisteredClientChangeDto dto) {
        checkClientName(dto, false);
        RegisteredClient appClient = BeanUtil.copyProperties(dto, RegisteredClient.class);
        appClient.setClientId(UUID.randomUUID().toString().replace("-", ""));
        appClient.setClientSecret(BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt()));

        boolean result = super.insert(appClient);
        return result ? Message.ok("创建成功") : Message.failed("创建失败");
    }

    @Override
    public Message<String> updateApp(RegisteredClientChangeDto dto) {
        checkClientName(dto, true);
        RegisteredClient appClient = BeanUtil.copyProperties(dto, RegisteredClient.class);

        boolean result = super.update(appClient);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    private void checkClientName(RegisteredClientChangeDto dto, boolean isEdit) {
        LambdaQuery<RegisteredClient> wrapper = new LambdaQuery<>();
        wrapper.eq(RegisteredClient::getClientName, dto.getClientName());
        if (isEdit) {
            wrapper.notEq(RegisteredClient::getId, dto.getId());
        }
        List<RegisteredClient> query = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(query)) {
            throw new BusinessException(50001, "该客户端名称已被使用，请重新输入");
        }
    }

    @Override
    public JpaPageResults<RegisteredClient> fetchPageResults(RegisteredClientPageDto dto) {
        RegisteredClient appClient = BeanUtil.copyProperties(dto, RegisteredClient.class);
        appClient.setDeleted("n");
        dto.build();
        return super.fetch(dto, appClient);
    }

	@Override
	public RegisteredClient findByClientId(String clientId) {
		return this.getMapper().findByClientId(clientId);
	}
}
