package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesChangeDto;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface AppResourcesService extends IJpaService<AppResources> {
    Message<String> create(AppResourcesChangeDto appResourcesChangeDto);
}
