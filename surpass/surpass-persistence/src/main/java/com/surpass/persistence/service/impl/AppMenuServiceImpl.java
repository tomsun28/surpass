package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppMenu;
import com.surpass.entity.app.dto.AppMenuPageDto;
import com.surpass.entity.permissions.Resources;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.AppMenuMapper;
import com.surpass.persistence.service.AppMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 15:25
 */

@Service
@RequiredArgsConstructor
public class AppMenuServiceImpl extends JpaServiceImpl<AppMenuMapper, AppMenu> implements AppMenuService{

    private final AppMenuMapper appMenuMapper;

    @Override
    public Message<JpaPageResults<AppMenu>> page(AppMenuPageDto dto) {
        if (StringUtils.isBlank(dto.getAppId())) {
            throw new BusinessException(50001, "未查询到该应用");
        }

        dto.build();
        JpaPageResults<AppMenu> jpaPageResults = (JpaPageResults<AppMenu>) this.buildPageResults(dto, appMenuMapper.pageList(dto));

        return Message.ok(jpaPageResults);
    }
}
