package com.surpass.persistence.service.impl;

import cn.hutool.core.lang.UUID;
import com.surpass.entity.RegisteredClientRelation;
import com.surpass.persistence.service.RegisteredClientRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.entity.dto.RegisteredClientChangeDto;
import com.surpass.entity.dto.RegisteredClientPageDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.RegisteredClientMapper;
import com.surpass.persistence.service.RegisteredClientService;
import com.surpass.util.StringGenerator;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/9 17:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegisteredClientServiceImpl extends JpaServiceImpl<RegisteredClientMapper, RegisteredClient> implements RegisteredClientService {

    private final RegisteredClientRelationService registeredClientRelationService;

	static final int SECRET_LENGTH = 32;

	@Override
    public Message<String> create(RegisteredClientChangeDto dto) {
        checkClientName(dto, false);
        RegisteredClient appClient = BeanUtil.copyProperties(dto, RegisteredClient.class);
        appClient.setClientId(UUID.randomUUID().toString().replace("-", ""));
        String clientSecret = PasswordReciprocal.getInstance().encode(new StringGenerator(SECRET_LENGTH).randomGenerate());
        appClient.setClientSecret(clientSecret);

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

	@Override
	public RegisteredClient generate(String id) {
		RegisteredClient client = get(id);
		if(client != null) {
			String clientSecret = new StringGenerator(SECRET_LENGTH).randomGenerate();
			String  encode = PasswordReciprocal.getInstance().encode(clientSecret);
			LambdaUpdateWrapper<RegisteredClient> updateWrapper = new LambdaUpdateWrapper<>();
			updateWrapper.set(RegisteredClient::getClientSecret, encode).eq(RegisteredClient::getId, id);
			this.update(updateWrapper);
			client.setClientSecret(clientSecret);
		}
		return client;
	}

    @Override
    @Transactional
    public Message<String> deleteClient(List<String> ids) {
        //删除客户端资源关系
        LambdaQuery<RegisteredClientRelation> registeredClientRelationLambdaQuery = new LambdaQuery<>();
        registeredClientRelationLambdaQuery.in(RegisteredClientRelation::getClientId, ids);
        registeredClientRelationService.delete(registeredClientRelationLambdaQuery);
        //删除客户端
        boolean result = super.softDelete(ids);

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }
}
