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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.constants.ConstsUser;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigInsuranceFund;
import com.surpass.entity.config.ConfigPersonalTax;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.hr.EmployeeSalary;
import com.surpass.entity.hr.EmployeeTaxDeduction;
import com.surpass.entity.hr.Employee;
import com.surpass.entity.hr.EmployeeSalaryTemp;
import com.surpass.entity.hr.dto.*;

import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.EmployeeSalaryTempService;
import com.surpass.util.BigDecimalUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/5 16:50
 */

@Slf4j
@Service
public class EmployeeSalaryTempServiceImpl extends ServiceImpl<EmployeeSalaryTempMapper, EmployeeSalaryTemp> implements EmployeeSalaryTempService {
    static final Logger logger = LoggerFactory.getLogger(EmployeeSalaryTempServiceImpl.class);

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    ConfigInsuranceFundMapper configInsuranceFundMapper;

    @Autowired
    EmployeeSalaryTempMapper employeeSalaryTempMapper;

    @Autowired
    EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    EmployeeTaxDeductionMapper employeeTaxDeductionMapper;

    @Autowired
    ConfigPersonalTaxMapper configPersonalTaxMapper;

    @Autowired
    ConfigSysService configSysService;


    @Override
    public Message<Page<EmployeeSalaryTemp>> pageList(SalaryDetailPageDto dto) {
        dto.setCurrentYearMonth(YearMonth.parse(configSysService.getCurrentTerm(dto.getBookId())));

        Page<EmployeeSalaryTemp> employeeSalaryTempPage = employeeSalaryTempMapper.pageList(dto.build(), dto);

        return Message.ok(employeeSalaryTempPage);
    }

    @Override
    @Transactional
    public Message<String> createFinalDetail(SalaryDetailPageDto dto) {
    	dto.setCurrentYearMonth(YearMonth.parse(configSysService.getCurrentTerm(dto.getBookId())));
        List<EmployeeSalaryTemp> employeeSalaryTemps = employeeSalaryTempMapper.listCurrentMonth(dto);

        //忽略字段
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreProperties("id", "createdBy", "createdDate", "modifiedBy", "modifiedDate", "deleted");
        List<EmployeeSalary> employeeSalaries = BeanUtil.copyToList(employeeSalaryTemps, EmployeeSalary.class, copyOptions);
        log.trace("Salary {}",employeeSalaries);
        if (ObjectUtils.isNotEmpty(employeeSalaries)) {
            //删除原先生成的本月数据
            YearMonth lastMonth = dto.getCurrentYearMonth();
            employeeSalaryMapper.delete(Wrappers.<EmployeeSalary>lambdaQuery()
                    .eq(EmployeeSalary::getBookId, dto.getBookId())
                    .eq(EmployeeSalary::getBelongDate, lastMonth));

            employeeSalaryMapper.insert(employeeSalaries);
        }
        return Message.ok("新增成功");
    }

    @Override
    public Message<EmployeeSalaryTemp> reCalculate(EmployeeSalaryTemp dto) {
        List<Employee> employees = employeeMapper.selectList(Wrappers.<Employee>lambdaQuery()
                .eq(Employee::getId, dto.getEmployeeId()));
        String bookId = dto.getBookId();
        if (ObjectUtils.isNotEmpty(employees)) {
            Employee employee = employees.get(0);

            //获取附加税抵扣
            Map<String, BigDecimal> taxDeduction = getTaxDeduction(List.of(employee), bookId);

            //获取社保公积金配置
            ConfigInsuranceFund configInsuranceFund;
            List<ConfigInsuranceFund> configInsuranceFunds = configInsuranceFundMapper.selectList(Wrappers.<ConfigInsuranceFund>lambdaQuery()
                    .eq(ConfigInsuranceFund::getBookId, bookId));
            if (ObjectUtils.isEmpty(configInsuranceFunds)) {
                throw new BusinessException(50001, "请在配置管理中添加社保公积金配置");
            } else {
                configInsuranceFund = configInsuranceFunds.get(0);
            }

            //赋值员工信息
            employee.setPayBasic(dto.getPayBasic());
            employee.setPayPost(dto.getPayPost());
            employee.setPayMerit(dto.getPayMerit());
            employee.setLaborFee(dto.getLaborFee());
            calculateOtherValue(configInsuranceFund, employee, dto, taxDeduction);

            //个税计算map
            Map<String, BigDecimal> needPersonalTaxMap = new HashMap<>();
            //兼职人员个税计算map
            Map<String, BigDecimal> needLaborTaxMap = new HashMap<>();

            if(employee.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.NORMAL)
                    ||employee.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.INTERN)
                    ||employee.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.RETIREMENT)) {
            	needPersonalTaxMap.put(employee.getId(), dto.getTaxableWages());

            	//计算个税
                Map<String, BigDecimal> personalTaxMap = calculatePersonalTax(needPersonalTaxMap, bookId, 0);
                // 设置个税
                String employeeId = dto.getEmployeeId();
                BigDecimal personalTax = personalTaxMap.getOrDefault(employeeId, BigDecimal.ZERO);
                dto.setPersonalTax(personalTax);
                dto.setTotalAmount(dto.getTotalAmount().subtract(dto.getPersonalTax()).max(BigDecimal.ZERO));
                dto.setBusinessExpenditureCosts(dto.getTotalAmount().add(personalTax).add(dto.getTotalSocialInsurance())
                        .add(dto.getProvidentFund()).add(dto.getBusinessSocialInsurance()).add(dto.getBusinessProvidentFund()));
            }else {
                needLaborTaxMap.put(employee.getId(), dto.getTaxableWages());
                //计算劳务个税
                Map<String, BigDecimal> stringBigDecimalMap = calculatePersonalTax(needLaborTaxMap, bookId, 1);
                // 设置个税
                String employeeId = dto.getEmployeeId();
                BigDecimal personalTax = stringBigDecimalMap.getOrDefault(employeeId, BigDecimal.ZERO);
                dto.setPersonalTax(personalTax);
                dto.setBusinessExpenditureCosts(dto.getTotalAmount());
                dto.setTotalAmount(dto.getTotalAmount().subtract(dto.getPersonalTax()).max(BigDecimal.ZERO));
            }
        }
        return Message.ok(dto);
    }

    @Override
    @Transactional
    public Message<String> createTable(CreateSalaryTableDto dto) {
        //账套ID
        String bookId = dto.getBookId();
        YearMonth lastMonth = YearMonth.parse(configSysService.getCurrentTerm(bookId));

        //获取没有计算当月数据的员工
        ListNoCalCurrentDto listNoCalCurrentDto = new ListNoCalCurrentDto();
        listNoCalCurrentDto.setBelongDate(lastMonth);
        listNoCalCurrentDto.setBookId(bookId);
        List<Employee> employeeList = employeeMapper.listNoCalCurrent(listNoCalCurrentDto);
       /* LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getBookId, bookId);
        List<Employee> employee = employeeMapper.selectList(employeeLambdaQueryWrapper);*/

        CalculateSalaryDto calculateSalaryDto = new CalculateSalaryDto(employeeList, bookId, lastMonth);

        List<EmployeeSalaryTemp> employeeSalaryTemps = calculateSalary(calculateSalaryDto);

/*        //删除之前的temp数据
        salaryDetailMapper.removeCurrentMonth(listNoCalCurrentDto);*/
        //批量新增
        boolean result = super.saveBatch(employeeSalaryTemps);
        if (ObjectUtils.isEmpty(employeeSalaryTemps)) {
            return Message.ok("新增成功");
        }
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }


    @Override
    public EmployeeSalaryTemp getById(Serializable id) {
        EmployeeSalaryTemp employeeSalaryTemp = super.getById(id);
        if (Objects.nonNull(employeeSalaryTemp)) {
            Employee employee = employeeMapper.selectById(employeeSalaryTemp.getEmployeeId());
            if (Objects.nonNull(employee)) {
                employeeSalaryTemp.setBankCardNo(employee.getBankCardNo());
                employeeSalaryTemp.setEmployeeName(employee.getDisplayName());
                employeeSalaryTemp.setEmployeeNumber(employee.getEmployeeNumber());
                employeeSalaryTemp.setEmployeeType(employee.getEmployeeType());
                employeeSalaryTemp.setBankName(employee.getBankName());
                return employeeSalaryTemp;
            }
            throw new BusinessException(50001, "查询不到该条员工数据");
        }

        throw new BusinessException(50001, "查询不到该条数据");
    }

    @Override
    @Transactional
    public Message<String> update(SalaryDetailChangeDto dto) {
        EmployeeSalaryTemp employeeSalaryTemp = BeanUtil.copyProperties(dto, EmployeeSalaryTemp.class);
        boolean result = super.updateById(employeeSalaryTemp);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    private Map<String, BigDecimal> getTaxDeduction(List<Employee> employees, String bookId) {
        if (ObjectUtils.isEmpty(employees) || ObjectUtils.isEmpty(bookId)) {
            return Collections.emptyMap();
        }

        List<String> idCardNos = employees.stream()
                .map(Employee::getIdCardNo)
                .toList();

        return employeeTaxDeductionMapper.selectList(Wrappers.<EmployeeTaxDeduction>lambdaQuery()
                        .eq(EmployeeTaxDeduction::getBookId, bookId)
                        .in(EmployeeTaxDeduction::getIdCardNo, idCardNos))
                .stream()
                .collect(Collectors.toMap(
                        EmployeeTaxDeduction::getIdCardNo,
                        e -> BigDecimal.valueOf(e.getTotalTaxDeduction()),
                        (existing, replacement) -> existing));
    }

    public List<EmployeeSalaryTemp> calculateSalary(CalculateSalaryDto dto) {
        String bookId = dto.getBookId();
        List<Employee> employees = dto.getEmployees();

        //获取社保公积金配置
        ConfigInsuranceFund configInsuranceFund;
        List<ConfigInsuranceFund> configInsuranceFunds = configInsuranceFundMapper.selectList(Wrappers.<ConfigInsuranceFund>lambdaQuery()
                .eq(ConfigInsuranceFund::getBookId, bookId));
        if (ObjectUtils.isEmpty(configInsuranceFunds)) {
            throw new BusinessException(50001, "请在配置管理中添加社保公积金配置");
        } else {
            configInsuranceFund = configInsuranceFunds.get(0);
        }

        //获取附加税抵扣
        Map<String, BigDecimal> taxDeduction = getTaxDeduction(employees, bookId);

        //工资明细集合
        List<EmployeeSalaryTemp> employeeSalaryTemps = new ArrayList<>();

        //个税计算map
        Map<String, BigDecimal> needPersonalTaxMap = new HashMap<>();

        //兼职人员个税计算map
        Map<String, BigDecimal> needLaborTaxMap = new HashMap<>();


        for (Employee employee : employees) {
            EmployeeSalaryTemp employeeSalaryTemp = new EmployeeSalaryTemp();
            employeeSalaryTemp.setBookId(bookId);

            employeeSalaryTemp.setEmployeeId(employee.getId());
            employeeSalaryTemp.setBelongDate(dto.getLastMonth());
            //设置基础工资
            employeeSalaryTemp.setPayBasic(employee.getPayBasic());
            employeeSalaryTemp.setPayPost(employee.getPayPost());
            employeeSalaryTemp.setPayMerit(employee.getPayMerit());
            employeeSalaryTemp.setLaborFee(employee.getLaborFee());
            employeeSalaryTemp.setEmployeeType(employee.getEmployeeType());

            //计算其他值
        	calculateOtherValue(configInsuranceFund, employee, employeeSalaryTemp, taxDeduction);

        	if(employeeSalaryTemp.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.NORMAL)
        			||employeeSalaryTemp.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.INTERN)
        			||employeeSalaryTemp.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.RETIREMENT)) {
        		needPersonalTaxMap.put(employee.getId(), employeeSalaryTemp.getTaxableWages());
        	} else {
                needLaborTaxMap.put(employee.getId(), employeeSalaryTemp.getTaxableWages());
            }

        	employeeSalaryTemps.add(employeeSalaryTemp);
        }

        //计算个税
        Map<String, BigDecimal> personalTaxMap = calculatePersonalTax(needPersonalTaxMap, bookId, 0);
        //计算劳务个税
        Map<String, BigDecimal> stringBigDecimalMap = calculatePersonalTax(needLaborTaxMap, bookId, 1);

        // 设置个税
        employeeSalaryTemps.forEach(salaryDetail -> {
            String employeeId = salaryDetail.getEmployeeId();
            BigDecimal personalTax = BigDecimal.valueOf(0);
            //工资 = 基本工资+岗位工资+绩效
        	BigDecimal salaryAmount = salaryDetail.getPayBasic()
	                .add(BigDecimalUtils.safeAdd(salaryDetail.getPayPost()))
	                .add(BigDecimalUtils.safeAdd(salaryDetail.getPayMerit()));
            if((salaryDetail.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.NORMAL)
        			||salaryDetail.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.INTERN)
        			||salaryDetail.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.RETIREMENT))
            		&& salaryAmount.compareTo(BigDecimal.ZERO)>0) {
	            personalTax = personalTaxMap.getOrDefault(employeeId, BigDecimal.ZERO);
	            salaryDetail.setPersonalTax(personalTax);
	            salaryDetail.setTotalAmount(salaryDetail.getTotalAmount().subtract(salaryDetail.getPersonalTax()).max(BigDecimal.ZERO));
	            salaryDetail.setBusinessExpenditureCosts(salaryDetail.getTotalAmount().add(personalTax).add(salaryDetail.getTotalSocialInsurance())
	                    .add(salaryDetail.getProvidentFund()).add(salaryDetail.getBusinessSocialInsurance()).add(salaryDetail.getBusinessProvidentFund()));
            }else {
                //计算兼职岗位个税
                personalTax = stringBigDecimalMap.getOrDefault(employeeId, BigDecimal.ZERO);
                salaryDetail.setPersonalTax(personalTax);
                salaryDetail.setBusinessExpenditureCosts(salaryDetail.getTotalAmount());
                salaryDetail.setTotalAmount(salaryDetail.getTotalAmount().subtract(salaryDetail.getPersonalTax()).max(BigDecimal.ZERO));
            }
        });

        return employeeSalaryTemps;
    }

    private void calculateOtherValue(ConfigInsuranceFund configInsuranceFund,
                                     Employee employee,
                                     EmployeeSalaryTemp employeeSalary,
                                     Map<String, BigDecimal> taxDeduction) {
    	//普通员工计算扣除费用
        if(employee.getEmployeeType().equals(ConstsUser.EMPLOYEE_TYPE.NORMAL)
        		||employee.getEmployeeType().equals(ConstsUser.EMPLOYEE_TYPE.INTERN)
        		||employee.getEmployeeType().equals(ConstsUser.EMPLOYEE_TYPE.RETIREMENT)) {
        	//工资 = 基本工资+岗位工资+绩效
        	BigDecimal salaryAmount = employee.getPayBasic()
	                .add(BigDecimalUtils.safeAdd(employee.getPayPost()))
	                .add(BigDecimalUtils.safeAdd(employee.getPayMerit()));


	        //个人工伤
	        BigDecimal employmentInjuryPersonalFee = BigDecimal.ZERO;
	        //个人养老
	        BigDecimal endowmentPersonalFee = BigDecimal.ZERO;
	        //个人医疗
	        BigDecimal medicalPersonalFee = BigDecimal.ZERO;
	        //个人生育
	        BigDecimal maternityPersonalFee = BigDecimal.ZERO;
	        //个人失业
	        BigDecimal unemploymentPersonalFee = BigDecimal.ZERO;
	        //个人公积金
	        BigDecimal providentFundSupPersonalFee = BigDecimal.ZERO;

	        //企业工伤
	        BigDecimal employmentInjuryBusinessFee = BigDecimal.ZERO;
	        //企业养老
	        BigDecimal endowmentBusinessFee = BigDecimal.ZERO;
	        //企业医疗
	        BigDecimal medicalBusinessFee = BigDecimal.ZERO;
	        //企业生育
	        BigDecimal maternityBusinessFee = BigDecimal.ZERO;
	        //大病医疗 固定
	        BigDecimal seriousMedicalBusiness= BigDecimal.ZERO;
	        BigDecimal seriousMedicalPersonal= BigDecimal.ZERO;
	        //企业失业
	        BigDecimal unemploymentBusinessFee = BigDecimal.ZERO;
	        //企业公积金
	        BigDecimal providentFundSupBusinessFee = BigDecimal.ZERO;

	        //实习生和退休返聘不需要计算社保公积金
	        if(employee.getEmployeeType().equals(ConstsUser.EMPLOYEE_TYPE.NORMAL)){
		        //设置社保公积金
		        if (employee.getPayBaseRule() == 1) {
		            //个人配置
		            BigDecimal payBaseNumber = employee.getPayBaseNumber();
		            employmentInjuryPersonalFee = calculateInsurance(payBaseNumber,configInsuranceFund.getEmploymentInjuryPersonalRate());
		            endowmentPersonalFee = calculateInsurance(payBaseNumber,configInsuranceFund.getEndowmentPersonalRate());
		            medicalPersonalFee = calculateInsurance(payBaseNumber,configInsuranceFund.getMedicalPersonalRate());
		            maternityPersonalFee = calculateInsurance(payBaseNumber,configInsuranceFund.getMaternityPersonalRate());
		            unemploymentPersonalFee = calculateInsurance(payBaseNumber,configInsuranceFund.getUnemploymentPersonalRate());
		            providentFundSupPersonalFee = calculateInsurance(payBaseNumber,configInsuranceFund.getProvidentFundSupPersonalRate());
		            seriousMedicalPersonal = configInsuranceFund.getSeriousMedicalPersonal();
		            //企业
		            employmentInjuryBusinessFee = calculateInsurance(payBaseNumber,configInsuranceFund.getEmploymentInjuryBusinessRate());
		            endowmentBusinessFee = calculateInsurance(payBaseNumber,configInsuranceFund.getEndowmentBusinessRate());
		            medicalBusinessFee = calculateInsurance(payBaseNumber,configInsuranceFund.getMedicalBusinessRate());
		            maternityBusinessFee = calculateInsurance(payBaseNumber,configInsuranceFund.getMaternityBusinessRate());
		            unemploymentBusinessFee = calculateInsurance(payBaseNumber,configInsuranceFund.getUnemploymentBusinessRate());
		            providentFundSupBusinessFee = calculateInsurance(payBaseNumber,configInsuranceFund.getProvidentFundSupBusinessRate());
		            seriousMedicalBusiness = configInsuranceFund.getSeriousMedicalBusiness();
		        } else {
		        	BigDecimal payBaseConfig = configInsuranceFund.getPayBase();
		            //系统配置
		            employmentInjuryPersonalFee = calculateInsurance(payBaseConfig,configInsuranceFund.getEmploymentInjuryPersonalRate());
		            endowmentPersonalFee = calculateInsurance(payBaseConfig,configInsuranceFund.getEndowmentPersonalRate());
		            medicalPersonalFee = calculateInsurance(payBaseConfig,configInsuranceFund.getMedicalPersonalRate());
		            maternityPersonalFee = calculateInsurance(payBaseConfig,configInsuranceFund.getMaternityPersonalRate());
		            unemploymentPersonalFee = calculateInsurance(payBaseConfig,configInsuranceFund.getUnemploymentPersonalRate());
		            providentFundSupPersonalFee = calculateInsurance(payBaseConfig,configInsuranceFund.getProvidentFundSupPersonalRate());
		            seriousMedicalPersonal = configInsuranceFund.getSeriousMedicalPersonal();
		            //企业
		            employmentInjuryBusinessFee = calculateInsurance(payBaseConfig,configInsuranceFund.getEmploymentInjuryBusinessRate());
		            endowmentBusinessFee = calculateInsurance(payBaseConfig,configInsuranceFund.getEndowmentBusinessRate());
		            medicalBusinessFee = calculateInsurance(payBaseConfig,configInsuranceFund.getMedicalBusinessRate());
		            maternityBusinessFee = calculateInsurance(payBaseConfig,configInsuranceFund.getMaternityBusinessRate());
		            unemploymentBusinessFee = calculateInsurance(payBaseConfig,configInsuranceFund.getUnemploymentBusinessRate());
		            providentFundSupBusinessFee = calculateInsurance(payBaseConfig,configInsuranceFund.getProvidentFundSupBusinessRate());
		            seriousMedicalBusiness = configInsuranceFund.getSeriousMedicalBusiness();
		        }
	        }

	        //设置个人社保三项
	        employeeSalary.setInsuranceEndowment(endowmentPersonalFee);
	        employeeSalary.setInsuranceMedical(medicalPersonalFee);
	        employeeSalary.setInsuranceUnemployment(unemploymentPersonalFee);
	        //工资大于0
	        if(salaryAmount.compareTo(BigDecimal.ZERO)>0) {
		        //总社保扣除
		        BigDecimal totalSocialInsurance = employmentInjuryPersonalFee
		                .add(endowmentPersonalFee)
		                .add(medicalPersonalFee)
		                .add(maternityPersonalFee)
		                .add(unemploymentPersonalFee)
		                .add(seriousMedicalPersonal);
		        employeeSalary.setTotalSocialInsurance(totalSocialInsurance);

		        //公积金
		        employeeSalary.setProvidentFund(providentFundSupPersonalFee);

		        //企业社保缴纳
		        BigDecimal socialInsuranceBusiness = employmentInjuryBusinessFee
		                .add(endowmentBusinessFee)
		                .add(medicalBusinessFee)
		                .add(maternityBusinessFee)
		                .add(unemploymentBusinessFee)
		                .add(seriousMedicalBusiness);
		        employeeSalary.setBusinessSocialInsurance(socialInsuranceBusiness);
		        employeeSalary.setBusinessProvidentFund(providentFundSupBusinessFee);
		        //应发工资 = 工资+应增-应扣
		        employeeSalary.setPayAmount(
		        		employee.getPayBasic()
		                .add(BigDecimalUtils.safeAdd(employee.getPayPost()))
		                .add(BigDecimalUtils.safeAdd(employee.getPayMerit()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getBonus()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getOvertime()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getAllowance()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getBackPay()))
		                .subtract(BigDecimalUtils.safeAdd(employeeSalary.getAttendance()))
		                .subtract(BigDecimalUtils.safeAdd(employeeSalary.getOtherDeductions()))
		        		);
		        //计算总工资(不含税)
		        BigDecimal totalAmount = employee.getPayBasic()
		                .add(BigDecimalUtils.safeAdd(employee.getPayPost()))
		                .add(BigDecimalUtils.safeAdd(employee.getPayMerit()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getBonus()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getOvertime()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getAllowance()))
		                .add(BigDecimalUtils.safeAdd(employeeSalary.getBackPay()))
		                .subtract(BigDecimalUtils.safeAdd(employeeSalary.getAttendance()))
		                .subtract(BigDecimalUtils.safeAdd(employeeSalary.getOtherDeductions()))
		                .subtract(totalSocialInsurance)
		                .subtract(providentFundSupPersonalFee);

		        //计算税务抵扣和应税工资
		        employeeSalary.setTaxDeduction(taxDeduction.getOrDefault(
		        		employee.getIdCardNo(),
		                BigDecimal.ZERO
		        ));

		        employeeSalary.setTaxableWages(
		                totalAmount.subtract(employeeSalary.getTaxDeduction()).max(BigDecimal.ZERO)
		        );

		        employeeSalary.setTotalAmount(totalAmount);
	        }else {
	        	employeeSalary.setTaxableWages(BigDecimal.ZERO);
	        	employeeSalary.setTotalAmount(BigDecimal.ZERO);
	        }
        }else {
            //其他兼职等人员，无需计算社保等
            BigDecimal laborFee = employee.getLaborFee();
            //劳务个人收入计算应纳税
            if (laborFee.compareTo(new BigDecimal("4000")) <= 0) {
                // 不超过4000元：应纳税所得额 = 收入 - 800
                employeeSalary.setTaxableWages(laborFee.subtract(new BigDecimal("800")));
            } else {
                // 超过4000元：应纳税所得额 = 收入 × 80%
                BigDecimal taxableWages = laborFee.multiply(new BigDecimal("0.8"));
                employeeSalary.setTaxableWages(taxableWages);
            }

            employeeSalary.setPayAmount(
            		laborFee
	                .add(BigDecimalUtils.safeAdd(employeeSalary.getBonus()))
	                .add(BigDecimalUtils.safeAdd(employeeSalary.getOvertime()))
	                .add(BigDecimalUtils.safeAdd(employeeSalary.getAllowance()))
	                .add(BigDecimalUtils.safeAdd(employeeSalary.getBackPay()))
	                .subtract(BigDecimalUtils.safeAdd(employeeSalary.getAttendance()))
	                .subtract(BigDecimalUtils.safeAdd(employeeSalary.getOtherDeductions()))
	        		);
        	 employeeSalary.setTotalAmount(laborFee);
        }
    }

    private BigDecimal calculateInsurance(BigDecimal payBase,BigDecimal rate) {
    	return payBase.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private Map<String, BigDecimal> calculatePersonalTax(Map<String, BigDecimal> taxableWagesMap, String bookId, Integer type) {
        // 获取税率表并按层级排序
        List<ConfigPersonalTax> taxRates =
        		configPersonalTaxMapper.selectList(
        				Wrappers.<ConfigPersonalTax>lambdaQuery()
        				.eq(ConfigPersonalTax::getType, type)
        				.orderByAsc(ConfigPersonalTax::getLevel));

        // 存放结果：工号 -> 个税金额
        Map<String, BigDecimal> personalTaxMap = new HashMap<>();

        // 遍历每个员工的应税工资
        taxableWagesMap.forEach((employeeId, taxableWages) -> {
            // 查找适用的税率区间，避免税率的 min 和 max 为 null 的情况
            ConfigPersonalTax applicableTax = taxRates.stream()
                    .filter(tax -> {
                        // 检查 tax.getMin() 和 tax.getMax() 是否为 null
                        BigDecimal min = tax.getMinNum() != null ? BigDecimal.valueOf(tax.getMinNum()) : BigDecimal.ZERO;
                        BigDecimal max = tax.getMaxNum() != null ? BigDecimal.valueOf(tax.getMaxNum()) : BigDecimal.valueOf(Double.MAX_VALUE); // 最大值作为默认

                        // 如果应税工资在 min 和 max 之间，返回 true
                        return taxableWages.compareTo(min) >= 0 && taxableWages.compareTo(max) <= 0;
                    })
                    .findFirst()
                    .orElse(null); // 如果找不到合适的税率区间，返回 null


            if (applicableTax != null) {
                // 计算个税 = 应税工资 × 税率 - 速算扣除数
                // 修改税率计算部分
                BigDecimal taxRate = BigDecimal.valueOf(applicableTax.getTaxRate())
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                BigDecimal quickDeduction = BigDecimal.valueOf(applicableTax.getCalculationDeduction());

                BigDecimal personalTax = taxableWages.multiply(taxRate)
                        .subtract(quickDeduction)
                        .setScale(2, RoundingMode.HALF_UP); // 保留两位小数

                personalTaxMap.put(employeeId, personalTax);
            } else {
                // 如果没有找到适用的税率区间，设置个税为0
                personalTaxMap.put(employeeId, BigDecimal.ZERO);
            }
        });

        return personalTaxMap;
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }



}
