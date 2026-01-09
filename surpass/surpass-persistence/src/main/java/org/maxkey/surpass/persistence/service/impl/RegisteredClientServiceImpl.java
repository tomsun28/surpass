package org.maxkey.surpass.persistence.service.impl;

import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.maxkey.surpass.crypto.password.PasswordReciprocal;
import org.maxkey.surpass.entity.ClientPermission;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.RegisteredClient;
import org.maxkey.surpass.entity.dto.RegisteredClientChangeDto;
import org.maxkey.surpass.entity.dto.RegisteredClientPageDto;
import org.maxkey.surpass.exception.BusinessException;
import org.maxkey.surpass.persistence.mapper.RegisteredClientMapper;
import org.maxkey.surpass.persistence.service.ClientPermissionService;
import org.maxkey.surpass.persistence.service.RegisteredClientService;
import org.maxkey.surpass.util.StringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
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

    private final ClientPermissionService registeredClientRelationService;

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
		RegisteredClient client = this.getMapper().findByClientId(clientId);
		if(StringUtils.isNotBlank(client.getIpWhiteList())) {
			List<String> ipList =Arrays.asList(StringUtils.split(client.getIpWhiteList(),","));
			HashSet<String>ipSets = new HashSet<>();
			for(String ipAddr : ipList) {
				ipSets.add(ipAddr);
			}
			client.setIpAddrSet(ipSets);
		}
		return client;
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
        LambdaQuery<ClientPermission> registeredClientRelationLambdaQuery = new LambdaQuery<>();
        registeredClientRelationLambdaQuery.in(ClientPermission::getClientId, ids);
        registeredClientRelationService.delete(registeredClientRelationLambdaQuery);
        //删除客户端
        boolean result = super.softDelete(ids);

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }

	@Override
	public void updateLastLoginTime(RegisteredClient client) {
		this.getMapper().updateLastLoginTime(client);
	}
}
