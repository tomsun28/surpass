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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.hr.EmployeeSalaryTemp;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.hr.dto.ListNoCalCurrentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/5 16:45
 */

@Mapper
public interface EmployeeSalaryTempMapper extends BaseMapper<EmployeeSalaryTemp> {
    Page<EmployeeSalaryTemp> pageList(Page page, @Param("Dto") SalaryDetailPageDto dto);

    List<EmployeeSalaryTemp> listCurrentMonth(@Param("Dto") SalaryDetailPageDto dto);

    int removeCurrentMonth(@Param("Dto") ListNoCalCurrentDto dto);
}
