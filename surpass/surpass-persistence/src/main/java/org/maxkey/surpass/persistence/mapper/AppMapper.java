package org.maxkey.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.maxkey.surpass.entity.app.App;

@Mapper
public interface AppMapper extends IJpaMapper<App> {
    App findByContextPath(String contextPath);

    App getById(String id);
}
