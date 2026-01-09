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




package org.maxkey.surpass.web.permissions.contorller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.maxkey.surpass.authn.annotation.CurrentUser;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.entity.permissions.Permission;
import org.maxkey.surpass.entity.permissions.dto.PermissionDto;
import org.maxkey.surpass.persistence.service.HistorySystemLogsService;
import org.maxkey.surpass.persistence.service.PermissionService;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/permissions/permission"})
public class PermissionController {
	static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

	@Autowired
	PermissionService permissionService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Permission> update(
			@RequestBody PermissionDto dto,
			@CurrentUser UserInfo currentUser) {
		logger.debug("-update  : {}" , dto);
		//have
		Permission queryPermission =
				new Permission(
						dto.roleId());
		List<Permission> permissionsList = permissionService.queryPermissions(queryPermission);

		HashMap<String,String >permissionsMap =new HashMap<>();
		for(Permission permission : permissionsList) {
			permissionsMap.put(permission.getUniqueId(),permission.getId());
		}
		//Maybe insert
		ArrayList<Permission> newPermissionsList =new ArrayList<>();
		HashMap<String,String >newPermissionsMap =new HashMap<>();
		for(String resourceId : dto.resourceIds()) {
		    Permission newPermission=new Permission(
		    			WebContext.genId(),
		    			dto.roleId(),
                    resourceId,
                    currentUser.getId());
		    newPermission.setId(newPermission.generateId());
		    newPermissionsMap.put(newPermission.getUniqueId(), dto.appId());

		    if(!dto.appId().equalsIgnoreCase(resourceId) &&
		            !permissionsMap.containsKey(newPermission.getUniqueId())) {
		    	newPermissionsList.add(newPermission);
		    }
		}

		//delete
		ArrayList<Permission> deletePermissionsList =new ArrayList<>();
		for(Permission deletePermission : permissionsList) {
           if(!newPermissionsMap.containsKey(deletePermission.getUniqueId())) {
        	   deletePermissionsList.add(deletePermission);
           }
        }
		if (!deletePermissionsList.isEmpty()) {
			logger.debug("-remove  : {}" , deletePermissionsList);
			permissionService.deleteGroupPrivileges(deletePermissionsList);
		}

		if (!newPermissionsList.isEmpty() && permissionService.insertGroupPrivileges(newPermissionsList)) {
			logger.debug("-insert  : {}" , newPermissionsList);
			return new Message<>(Message.SUCCESS);

		} else {
			return new Message<>(Message.SUCCESS);
		}

	}

    @GetMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<List<Permission>> get(
    		@ModelAttribute Permission permission,
    		@CurrentUser UserInfo currentUser) {
        logger.debug("-get  : {}" , permission);
        //have
        Permission queryPermission =
        		new Permission(
        				permission.getRoleId());

        List<Permission> queryPermissionList = permissionService.queryPermissions(queryPermission);

        return new Message<>(queryPermissionList);
	}


}
