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


package com.surpass.persistence.service;

import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.hr.EmployeeSalary;
import com.surpass.entity.hr.EmployeeSalarySummary;
import com.surpass.entity.hr.dto.SalaryDetailChangeDto;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.hr.dto.SalarySummaryChangeDto;

import jakarta.servlet.http.HttpServletResponse;

public interface EmployeeSalaryService extends IService<EmployeeSalary> {
    Message<Page<EmployeeSalary>> pageList(SalaryDetailPageDto dto);

    Message<String> update(SalaryDetailChangeDto dto);

    Message<String> save(SalaryDetailChangeDto dto);

    Message<String> delete(ListIdsDto dto);

    EmployeeSalarySummary selectSalarySummary(@Param("Dto") SalarySummaryChangeDto dto);


    Message<String> exportTaxItems(SalaryDetailPageDto dto, HttpServletResponse response);

    Message<String> generateVoucher(GenerateVoucherDto dto);

    Message<String> deleteVoucher(GenerateVoucherDto dto);
}
