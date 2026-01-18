package org.dromara.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.entity.api.ApiPublishRecord;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ApiPublishMapper extends IJpaMapper<ApiPublishRecord> {
    List<ApiPublishRecord> findByApiIdOrderByPublishTimeDesc(@Param("apiId") String apiId);
}
