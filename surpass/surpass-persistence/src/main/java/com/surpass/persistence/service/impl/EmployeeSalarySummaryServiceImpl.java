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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.hr.EmployeeSalarySummary;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.hr.dto.SalarySummaryChangeDto;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.EmployeeSalarySummaryService;
import com.surpass.persistence.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/27 17:45
 */

@Service

public class EmployeeSalarySummaryServiceImpl extends ServiceImpl<EmployeeSalarySummaryMapper, EmployeeSalarySummary> implements EmployeeSalarySummaryService {

    @Autowired
    EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    VoucherService voucherService;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookSubjectMapper bookSubjectMapper;

    @Autowired
    ConfigSysService configSysService;

    @Override
    @Transactional
    public Message<String> save(SalarySummaryChangeDto dto) {
        YearMonth lastMonth = YearMonth.parse(configSysService.getCurrentTerm(dto.getBookId()));
        dto.setBelongDate(lastMonth);
        int count = employeeSalaryMapper.countEmployeeSalaries(dto);
        if (count > 0) {
            super.remove(Wrappers.<EmployeeSalarySummary>lambdaQuery()
                    .eq(EmployeeSalarySummary::getBookId, dto.getBookId())
                    .eq(EmployeeSalarySummary::getBelongDate, lastMonth));

            //员工费用
            boolean result =false;
            EmployeeSalarySummary employeeSalarySummary = employeeSalaryMapper.selectSalarySummary(dto);
            if(employeeSalarySummary != null) {
            	result = super.save(employeeSalarySummary);
            }

            //兼职费用
            employeeSalarySummary = employeeSalaryMapper.selectSalarySummaryLabor(dto);
            if(employeeSalarySummary != null) {
            	result = super.save(employeeSalarySummary);
            }
            return result ? Message.ok("成功") : Message.failed("失败");
        }

        return Message.failed("暂无数据，请先计算当月工资然后推送工资明细");
    }

    @Override
    public Message<Page<EmployeeSalarySummary>> pageList(SalaryDetailPageDto dto) {
        LambdaQueryWrapper<EmployeeSalarySummary> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(dto.getLabel())) {
            wrapper.like(EmployeeSalarySummary::getLabel, dto.getLabel());
        }
        wrapper.eq(EmployeeSalarySummary::getBookId, dto.getBookId());
        if (ObjectUtils.isNotEmpty(dto.getBelongDateRange()) && dto.getBelongDateRange().length >= 2) {
            String startDate = dto.getBelongDateRange()[0];
            String endDate = dto.getBelongDateRange()[1];

            wrapper.ge(EmployeeSalarySummary::getBelongDate, startDate)
                    .le(EmployeeSalarySummary::getBelongDate, endDate);
        }
        wrapper.orderByDesc(EmployeeSalarySummary::getBelongDate);
        Page<EmployeeSalarySummary> page = super.page(dto.build(), wrapper);
        return Message.ok(page);
    }



    @Override
    public EmployeeSalarySummary selectSalarySummary(SalarySummaryChangeDto dto) {
        return this.baseMapper.selectSalarySummary(dto);
    }

}
