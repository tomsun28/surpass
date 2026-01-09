package org.maxkey.surpass.persistence.service;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.RegisteredClient;
import org.maxkey.surpass.entity.dto.RegisteredClientChangeDto;
import org.maxkey.surpass.entity.dto.RegisteredClientPageDto;

import java.util.List;

public interface RegisteredClientService extends IJpaService<RegisteredClient> {
    Message<String> create(RegisteredClientChangeDto dto);

    Message<String> updateApp(RegisteredClientChangeDto dto);

    JpaPageResults<RegisteredClient> fetchPageResults(RegisteredClientPageDto dto);

    public RegisteredClient findByClientId(String clientId);

    public RegisteredClient generate(String id);

    Message<String> deleteClient(List<String> ids);
    
    public void updateLastLoginTime(RegisteredClient client);

}
