/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */






package org.dromara.surpass.persistence.service.impl;

import java.util.List;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.entity.permissions.Permission;
import org.dromara.surpass.persistence.mapper.PermissionMapper;
import org.dromara.surpass.persistence.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionServiceImpl extends JpaServiceImpl<PermissionMapper,Permission> implements PermissionService {
	static final  Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

	@Autowired
	PermissionMapper permissionMapper;

	public PermissionMapper getMapper() {
		return permissionMapper;
	}

	public boolean insertGroupPrivileges(List<Permission> permissionsList) {
	    return getMapper().insertPermissions(permissionsList)>0;
	}

	public boolean deleteGroupPrivileges(List<Permission> permissionsList) {
	     return getMapper().deletePermissions(permissionsList)>=0;
	 }

    public List<Permission> queryPermissions(Permission rolePermissions){
        return getMapper().queryPermissions(rolePermissions);
    }

}
