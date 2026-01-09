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

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.entity.permissions.Resources;
import org.maxkey.surpass.entity.permissions.dto.ResourcesPageDto;
import org.maxkey.surpass.persistence.mapper.ResourcesMapper;
import org.maxkey.surpass.persistence.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResourcesServiceImpl  extends JpaServiceImpl<ResourcesMapper,Resources> implements ResourcesService {

	@Autowired
	ResourcesMapper resourcesMapper;

	public List<Resources> queryResourcesTree(Resources resource){
	   return  resourcesMapper.queryResourcesTree(resource);
	}

	@Override
	public Message<JpaPageResults<Resources>> pageList(ResourcesPageDto dto) {
		dto.build();
		JpaPageResults<Resources> jpaPageResults = (JpaPageResults<Resources>) this.buildPageResults(dto, resourcesMapper.pageList(dto));
		return new Message<>(Message.SUCCESS, jpaPageResults);
	}
}
