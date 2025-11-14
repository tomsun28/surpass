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
import com.surpass.entity.dto.InstitutionsPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.surpass.entity.Institutions;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.springframework.data.domain.Page;

@Mapper
public interface InstitutionsMapper extends IJpaMapper<Institutions> {

	@Select("select * from  surpass_institutions where deleted = 'n' and id = #{value} or domain = #{value}  or console_domain = #{value}" )
	Institutions getByInstIdOrDomain(String instIdOrDomain);
}
