package org.dromara.surpass.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.entity.ClientPermission;
import org.dromara.surpass.entity.app.AppResources;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/11 10:15
 */

@Mapper
public interface ClientPermissionMapper extends IJpaMapper<ClientPermission> {

	public List<AppResources> queryPermissions(ClientPermission clientPermission);
	
}
