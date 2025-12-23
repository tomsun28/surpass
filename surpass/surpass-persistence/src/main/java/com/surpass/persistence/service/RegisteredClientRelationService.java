package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClientRelation;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.ClientAuthzDto;
import com.surpass.entity.dto.RegisteredClientRelationDto;

import org.dromara.mybatis.jpa.service.IJpaService;

import java.util.List;

public interface RegisteredClientRelationService extends IJpaService<RegisteredClientRelation> {
    List<RegisteredClientRelation> getClientApps(String clientId);

    Message<String> saveClientAppRelation(ClientAuthzDto dto);

    Message<List<RegisteredClientRelation>> getClientAuthz(AppResourcesPageDto dto);
}
