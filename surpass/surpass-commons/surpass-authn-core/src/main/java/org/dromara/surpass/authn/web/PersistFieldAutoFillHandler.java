/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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




package org.dromara.surpass.authn.web;

import org.apache.ibatis.reflection.MetaObject;
import org.dromara.mybatis.jpa.handler.FieldAutoFillHandler;
import org.dromara.surpass.authn.SignedPrincipal;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 24096
 */
@Component
public class PersistFieldAutoFillHandler extends FieldAutoFillHandler {

	@Override
	public void insertFill(MetaObject metaObject) {

		SignedPrincipal principal = getPrincipal();
		if(principal != null) {
			this.setFieldValue(metaObject , "instId", principal.getInstId());
			this.setFieldValue(metaObject , "createdBy", principal.getUserId());
		}
		this.setFieldValue(metaObject , "createdDate", new Date());

	}

	@Override
	public void updateFill(MetaObject metaObject) {
		SignedPrincipal principal = getPrincipal();
		if(principal != null) {
			this.setFieldValue(metaObject , "modifiedBy", principal.getUserId());
		}
		this.setFieldValue(metaObject , "modifiedDate", new Date());
	}

	/**
	 * 获取principal , 忽略异常情况
	 * @return
	 */
	SignedPrincipal getPrincipal() {
		SignedPrincipal principal = null;
		try {
			principal = AuthorizationUtils.getPrincipal();
		}catch(Exception e) {
			//
		}
		return principal;
	}
}
