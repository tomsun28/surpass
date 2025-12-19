package com.surpass.persistence.mapper;

import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

import java.util.List;

@Mapper
public interface AppResourcesMapper extends IJpaMapper<AppResources> {
    List<AppResources> pageList(AppResourcesPageDto dto);
}
