package com.surpass.persistence.mapper;

import com.surpass.entity.api.ApiPublishRecord;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ApiPublishMapper extends IJpaMapper<ApiPublishRecord> {
    List<ApiPublishRecord> findByApiIdOrderByPublishTimeDesc(@Param("apiId") String apiId);
}
