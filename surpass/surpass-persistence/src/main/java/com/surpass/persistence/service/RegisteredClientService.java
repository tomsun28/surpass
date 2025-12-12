package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.entity.dto.RegisteredClientChangeDto;
import com.surpass.entity.dto.RegisteredClientPageDto;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface RegisteredClientService extends IJpaService<RegisteredClient> {
    Message<String> create(RegisteredClientChangeDto dto);

    Message<String> updateApp(RegisteredClientChangeDto dto);

    JpaPageResults<RegisteredClient> fetchPageResults(RegisteredClientPageDto dto);

}
