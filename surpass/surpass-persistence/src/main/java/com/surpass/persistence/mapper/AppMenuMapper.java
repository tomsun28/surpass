package com.surpass.persistence.mapper;

import com.surpass.entity.app.AppMenu;
import com.surpass.entity.app.dto.AppMenuPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mybatis.jpa.IJpaMapper;

import java.util.List;

@Mapper
public interface AppMenuMapper extends IJpaMapper<AppMenu> {
    List<AppMenu> pageList(@Param("Dto") AppMenuPageDto dto);
}
