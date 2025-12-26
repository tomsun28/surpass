package com.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.surpass.entity.ClientPermission;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/11 10:15
 */

@Mapper
public interface ClientPermissionMapper extends IJpaMapper<ClientPermission> {

}
