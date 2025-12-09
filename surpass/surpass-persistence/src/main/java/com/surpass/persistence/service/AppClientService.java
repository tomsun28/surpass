package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import com.surpass.entity.app.AppClient;
import com.surpass.entity.app.dto.AppChangeDto;
import com.surpass.entity.app.dto.AppClientChangeDto;
import com.surpass.entity.app.dto.AppClientPageDto;
import com.surpass.entity.app.dto.AppPageDto;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface AppClientService extends IJpaService<AppClient> {
    Message<String> create(AppClientChangeDto dto);

    Message<String> updateApp(AppClientChangeDto dto);

    JpaPageResults<AppClient> fetchPageResults(AppClientPageDto dto);

}
