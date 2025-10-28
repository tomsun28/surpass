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






package com.surpass.persistence.mapper;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.idm.dto.OrgPageDto;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.surpass.entity.idm.Organizations;

public interface OrganizationsMapper extends BaseMapper<Organizations> {

	List<Organizations> queryOrgs(Organizations organization);


	Page<Organizations> pageList(Page page, @Param("Dto") OrgPageDto dto);
}
