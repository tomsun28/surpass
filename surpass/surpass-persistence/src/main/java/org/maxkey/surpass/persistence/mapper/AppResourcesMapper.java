package org.maxkey.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.maxkey.surpass.entity.app.AppResources;
import org.maxkey.surpass.entity.app.dto.AppResourcesPageDto;

import java.util.List;

@Mapper
public interface AppResourcesMapper extends IJpaMapper<AppResources> {
    List<AppResources> pageList(AppResourcesPageDto dto);

    List<String> selectAllDescendantIds(@Param("rootId") String rootId);

}
