package com.surpass.persistence.mapper;

import com.surpass.entity.app.App;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

@Mapper
public interface AppMapper extends IJpaMapper<App> {
    App findByContextPath(String contextPath);
}
