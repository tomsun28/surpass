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
import com.surpass.entity.hr.EmployeeSalary;
import com.surpass.entity.hr.EmployeeSalarySummary;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.hr.dto.SalarySummaryChangeDto;
import com.surpass.entity.hr.vo.TaxDeductionExportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/20 11:56
 */

@Mapper
public interface EmployeeSalaryMapper extends BaseMapper<EmployeeSalary> {
    Page<EmployeeSalary> pageList(Page page, @Param("Dto") SalaryDetailPageDto dto);

    EmployeeSalarySummary selectSalarySummary(@Param("Dto") SalarySummaryChangeDto dto);

    EmployeeSalarySummary selectSalarySummaryLabor(@Param("Dto") SalarySummaryChangeDto dto);

    int countEmployeeSalaries(@Param("Dto") SalarySummaryChangeDto dto);

    List<TaxDeductionExportVo> exportGetSalaryDetail(@Param("Dto") SalaryDetailPageDto dto);
}
