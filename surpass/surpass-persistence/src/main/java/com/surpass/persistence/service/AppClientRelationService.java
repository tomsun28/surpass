package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppClientRelation;
import com.surpass.entity.app.dto.AppClientChangeDto;
import com.surpass.entity.app.dto.AppClientRelationDto;
import org.dromara.mybatis.jpa.service.IJpaService;

import java.util.List;

public interface AppClientRelationService extends IJpaService<AppClientRelation> {
    List<AppClientRelation> getClientApps(String clientId);

    Message<String> saveClientAppRelation(AppClientRelationDto dto);
}
