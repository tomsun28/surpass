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






package com.surpass.persistence.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.SocialsAssociate;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.mapper.SocialsAssociateMapper;
import com.surpass.persistence.service.SocialsAssociatesService;


@Repository
public class SocialsAssociatesServiceImpl  extends ServiceImpl<SocialsAssociateMapper,SocialsAssociate> implements SocialsAssociatesService {

	@Autowired
	SocialsAssociateMapper socialsAssociateMapper;

	public SocialsAssociateMapper getMapper() {
		return socialsAssociateMapper;
	}

	public List<SocialsAssociate>  queryByUser(UserInfo user) {
		return getMapper().queryByUser(user);
	}

}
