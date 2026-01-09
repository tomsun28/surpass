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






package org.maxkey.surpass.persistence.service.impl;

import java.util.Date;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.maxkey.surpass.entity.ChangePassword;
import org.maxkey.surpass.entity.history.HistorySystemLogs;
import org.maxkey.surpass.entity.idm.Organizations;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.entity.permissions.Permission;
import org.maxkey.surpass.entity.permissions.Resources;
import org.maxkey.surpass.entity.permissions.RoleMember;
import org.maxkey.surpass.entity.permissions.Roles;
import org.maxkey.surpass.persistence.mapper.HistorySystemLogsMapper;
import org.maxkey.surpass.persistence.service.HistorySystemLogsService;
import org.maxkey.surpass.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistorySystemLogsServiceImpl  extends JpaServiceImpl<HistorySystemLogsMapper,HistorySystemLogs> implements HistorySystemLogsService {
	static final Logger logger = LoggerFactory.getLogger(HistorySystemLogsServiceImpl.class);

	@Autowired
	HistorySystemLogsMapper historySystemLogsMapper;

	public HistorySystemLogsMapper getMapper() {
		return historySystemLogsMapper;
	}

	public void log(String topic,Object entity,String action,String result,UserInfo operator) {
		String message = "";
		String targetId = "";
		String targetName = "";
		String cipherText = "";
		if(entity != null) {
			if(entity instanceof UserInfo model) {
				targetId = model.getId();
				targetName =  model.getUsername();
				message = buildMsg(model);
			}else if(entity instanceof Organizations model) {
				targetId = model.getId();
				targetName =  model.getOrgName();
				message = buildMsg(model);
			}else if(entity instanceof ChangePassword model) {
				targetId = model.getId();
				targetName =  model.getUsername();
				cipherText = model.getPassword();
				message = buildMsg(model);
			} else if(entity instanceof Roles model) {
				targetId = model.getId();
				targetName =  model.getRoleName();
				message = buildMsg(model);
			}else if(entity instanceof RoleMember model) {
				targetId = model.getId();
				targetName =  model.getRoleName();
				message = buildMsg(model);
			}else if(entity instanceof Resources model) {
				message = buildMsg(model);
			}else if(entity instanceof Permission model) {
				targetId = model.getId();
				targetName =  model.getRoleId();
				message = buildMsg(model);
			}else if(entity instanceof String) {
				message = entity.toString();
			}else {
				message = entity.toString();
			}

		}

		log(topic,targetId,targetName,cipherText,message,action,result,operator, entity);
	}

	public void log(String topic,String targetId,String targetName,String cipherText ,String message,String action,String result,UserInfo operator,Object entity) {
		HistorySystemLogs systemLog = new HistorySystemLogs();
		//systemLog.setId(systemLog.generateId());
		systemLog.setTargetId(targetId);
		systemLog.setTargetName(targetName);
		systemLog.setCipherText(cipherText);
		systemLog.setTopic(topic);
		systemLog.setMessage(message);
		systemLog.setMessageAction(action);
		systemLog.setMessageResult(result);
		systemLog.setUserId(operator.getId());
		systemLog.setUsername(operator.getUsername());
		systemLog.setDisplayName(operator.getDisplayName());
		systemLog.setJsonCotent(JsonUtils.toString(entity));
		systemLog.setExecuteTime(new Date());
		logger.trace("System Log {}" ,systemLog);
		getMapper().insert(systemLog);
	}

	public String buildMsg(UserInfo userInfo) {
		return new StringBuilder()
				.append(userInfo.getDisplayName())
				.append("[")
				.append(userInfo.getUsername())
				.append("]")
				.toString();
	}

	public String buildMsg(Organizations org) {
		return new StringBuilder()
				.append(org.getOrgName())
				.append("[")
				.append(org.getOrgCode())
				.append("]")
				.toString();
	}

	public String buildMsg(ChangePassword changePassword) {
		return new StringBuilder()
				.append(changePassword.getDisplayName())
				.append("[")
				.append(changePassword.getUsername())
				.append("]")
				.toString();
	}

	public String buildMsg(Roles g) {
		return new StringBuilder()
				.append(g.getRoleName())
				.toString();
	}


	public String buildMsg(RoleMember rm) {
		return new StringBuilder()
				.append(rm.getRoleName())
				.append("[")
				.append(rm.getUsername()).append(",")
				.append(rm.getDisplayName())
				.append("]")
				.toString();
	}

	public String buildMsg(Permission privilege) {
		return new StringBuilder()
				.append(privilege.getRoleId())
				.append("[")
				.append(privilege.getResourceId())
				.append("]")
				.toString();
	}


	public String buildMsg(Resources r) {
		return new StringBuilder()
				.append(r.getResName())
				.append("[")
				.append(r.getClassify())
				.append("]")
				.toString();
	}

}
