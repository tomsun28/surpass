package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppMenu;
import com.surpass.entity.app.dto.AppMenuChangeDto;
import com.surpass.entity.app.dto.AppMenuPageDto;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface AppMenuService extends IJpaService<AppMenu> {
    Message<JpaPageResults<AppMenu>> page(AppMenuPageDto dto);

    Message<String> save(AppMenuChangeDto dto);

    Message<String> update(AppMenuChangeDto dto);
}
