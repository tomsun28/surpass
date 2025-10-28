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




package com.surpass.authn.web;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.surpass.authn.SignedPrincipal;

/**
 * @author 24096
 */
@Component
public class PersistFieldAutoFillHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.fillStrategy(metaObject , "createdDate", new Date());
		this.fillStrategy(metaObject , "modifiedDate", new Date());
		this.fillStrategy(metaObject, "deleted", "n");
		try {
			SignedPrincipal principal = getPrincipal();
			if(principal != null) {
				this.fillStrategy(metaObject , "createdBy", principal.getUserId());
				this.fillStrategy(metaObject , "modifiedBy", principal.getUserId());
			}
		} catch (Exception e) {
			this.fillStrategy(metaObject , "createdBy", "0");
			this.fillStrategy(metaObject , "modifiedBy", "0");
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		try {
			SignedPrincipal principal = getPrincipal();
			if(principal != null) {
				this.fillStrategy(metaObject , "modifiedBy", principal.getUserId());
			}
			this.setFieldValByName("modifiedDate", new Date(), metaObject);
		} catch (Exception e) {
			this.setFieldValByName("modifiedDate", new Date(), metaObject);
			this.fillStrategy(metaObject , "modifiedBy", "0");
		}

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
