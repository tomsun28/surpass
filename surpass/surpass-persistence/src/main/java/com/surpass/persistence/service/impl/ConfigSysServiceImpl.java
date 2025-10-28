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

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surpass.constants.ConstsSysConfig;
import com.surpass.entity.Message;
import com.surpass.entity.PageQuery;
import com.surpass.entity.config.ConfigSys;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.enums.YesNoEnum;
import com.surpass.exception.ServiceException;
import com.surpass.persistence.mapper.ConfigSysMapper;
import com.surpass.persistence.service.ConfigService;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.util.DateUtils;
import com.surpass.util.SpringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;

import static com.surpass.constants.ConstsCached.CONFIG_SYS;

/**
 * 参数配置 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class ConfigSysServiceImpl implements ConfigSysService, ConfigService {

    private final ConfigSysMapper baseMapper;
    public static final List<String> BOOKS_KEYS = List.of(
            ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT,
            ConstsSysConfig.SYS_PAYMENT_TERM_START,
            ConstsSysConfig.SYS_INITIALIZE_TASK,
            ConstsSysConfig.SYS_SUBJECT_LAVAL,
            ConstsSysConfig.SYS_SUBJECT_CODES_LENGTH,
            ConstsSysConfig.SYS_DEFAULT_ACCOUNTS_PAYABLE,
            ConstsSysConfig.SYS_DEFAULT_ACCOUNTS_RECEIVABLE,
            ConstsSysConfig.SYS_DEFAULT_SHORT_TERM_ACCOUNTS_PAYABLE,
            ConstsSysConfig.SYS_DEFAULT_SHORT_TERM_ACCOUNTS_RECEIVABLE,
            ConstsSysConfig.SYS_DEFAULT_TRADING_FINANCIAL_ASSETS,
            ConstsSysConfig.SYS_DEFAULT_BILL_RECEIVABLE,
            ConstsSysConfig.SYS_DEFAULT_CURRENT_LIABILITIES,
            ConstsSysConfig.SYS_DEFAULT_CURRENT_ASSETS,
            ConstsSysConfig.SYS_DEFAULT_CURRENT_ASSETS_INVENTORY,
            ConstsSysConfig.SYS_DEFAULT_CASH_SUBJECT_CODES,
            ConstsSysConfig.SYS_DEFAULT_INCOME_NET_PROFIT,
            ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_REVENUE,
            ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_COSTS,
            ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_PROFIT,
            ConstsSysConfig.SYS_DEFAULT_ADMINISTRATIVE_EXPENSES,
            ConstsSysConfig.SYS_DEFAULT_SELLING_EXPENSES,
            ConstsSysConfig.SYS_DEFAULT_FINANCIAL_EXPENSES,
            ConstsSysConfig.SYS_DEFAULT_ADDED_TAX,
            ConstsSysConfig.SYS_DEFAULT_INCOME_TAX_EXPENSES
    );
    public static final List<String> BOOKS_SHOW_KEYS = Arrays.asList(
            ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT,
            ConstsSysConfig.SYS_PAYMENT_TERM_START,
            ConstsSysConfig.SYS_INITIALIZE_TASK,
            ConstsSysConfig.SYS_SUBJECT_LAVAL,
            ConstsSysConfig.SYS_SUBJECT_CODES_LENGTH
    );

    @Override
    public Message<List<ConfigSys>> pageList(ConfigSys config, PageQuery pageQuery) {
        LambdaQueryWrapper<ConfigSys> lqw = new LambdaQueryWrapper<ConfigSys>()
                .eq(ConfigSys::getBookId, config.getBookId())
                .in(ConfigSys::getConfigKey, BOOKS_SHOW_KEYS);
        List<ConfigSys> page = baseMapper.selectList(lqw);
        return Message.ok(page);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public Message<ConfigSys> getById(String configId) {
        return Message.ok(baseMapper.selectById(configId));
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param bookId    账套ID
     * @param configKey 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = CONFIG_SYS, key = "#bookId + '_' + #configKey")
    @Override
    public String selectConfigByKey(String bookId, String configKey) {
        ConfigSys retConfig = baseMapper.selectOne(new LambdaQueryWrapper<ConfigSys>()
                .eq(ConfigSys::getBookId, bookId)
                .eq(ConfigSys::getConfigKey, configKey));
        if (ObjectUtil.isNotNull(retConfig)) {
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取当前期
     */
    @Override
    public String getCurrentTerm(String bookId) {
        String currentTerm = this.selectConfigByKey(bookId, ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT);
        if (StringUtils.isEmpty(currentTerm)) {
            throw new ServiceException("账套未初始化：“当前账期”参数异常");
        }
        return currentTerm;
    }

    /**
     * 获取下一期
     */
	@Override
	public String getPrevTerm(String bookId) {
		String currentTerm = getCurrentTerm(bookId);
		YearMonth currentTermYearMonth = YearMonth.parse(currentTerm);
		return currentTermYearMonth.minusMonths(1).toString();
	}

    /**
     * 获取下一期
     */
	@Override
	public String getNextTerm(String bookId) {
		String currentTerm = getCurrentTerm(bookId);
		YearMonth currentTermYearMonth = YearMonth.parse(currentTerm);
		return currentTermYearMonth.plusMonths(1).toString();
	}

	/**
	 * 当期月末日期
	 */
	@Override
	public Date getCurrentTermLastDate(String bookId) {
		return DateUtils.parse(getCurrentTermLastDateString(bookId), DateUtils.FORMAT_DATE_YYYY_MM_DD);
	}

	/**
	 * 当期月末日期
	 */
	@Override
	public String getCurrentTermLastDateString(String bookId) {
		return DateUtils.lastDay(getCurrentTerm(bookId) + "-01").toString();
	}

	/**
	 * 账套初始期
	 */
    @Override
    public String getTermStart(String bookId) {
        String term = this.selectConfigByKey(bookId, ConstsSysConfig.SYS_PAYMENT_TERM_START);
        if (StringUtils.isEmpty(term)) {
            throw new ServiceException("账套未初始化：“初始账期”参数异常");
        }
        return term;
    }

	@Override
	public Message<String> termToNext(String bookId) {
		String nextTerm =  getNextTerm(bookId);
		return updateCurrentTerm(bookId,nextTerm);
	}

    @Override
    public Message<String> updateCurrentTerm(String bookId, String currentTerm) {
        Message<List<ConfigSys>> mlc = getBookConfigList(bookId);
        Message<String> msg = Message.failed("设置失败");
        for (ConfigSys configSys : mlc.getData()) {
            if (ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT.equals(configSys.getConfigKey())) {
                configSys.setConfigValue(currentTerm);
                msg = this.update(configSys);
            }
        }

        return msg;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public Message<List<ConfigSys>> selectConfigList(ConfigSys config) {
        LambdaQueryWrapper<ConfigSys> lqw = new LambdaQueryWrapper<ConfigSys>()
                .like(StringUtils.isNotBlank(config.getConfigName()), ConfigSys::getConfigName, config.getConfigName())
                .eq(StringUtils.isNotBlank(config.getConfigType()), ConfigSys::getConfigType, config.getConfigType())
                .eq(StringUtils.isNotBlank(config.getBookId()), ConfigSys::getBookId, config.getBookId())
                .like(StringUtils.isNotBlank(config.getConfigKey()), ConfigSys::getConfigKey, config.getConfigKey());
        return Message.ok(baseMapper.selectList(lqw));
    }

    /**
     * 初始化账套参数配置
     *
     * @param bookId 账簿ID
     * @return 结果
     */
    @Override
    @Transactional
    public Message<Boolean> initBooksConfig(String bookId, String currentTerm) {
        LambdaQueryWrapper<ConfigSys> lqw = new LambdaQueryWrapper<ConfigSys>()
                .eq(ConfigSys::getBookId, ConstsSysConfig.SYS_CONFIG_TEMPLATE_ID);
        List<ConfigSys> data = baseMapper.selectList(lqw);
        for (ConfigSys datum : data) {
            datum.setBookId(bookId);
            datum.setConfigId(null);
            datum.setCreatedDate(null);
            datum.setCreatedBy(null);
            datum.setModifiedDate(null);
            datum.setModifiedBy(null);
            if(StringUtils.isBlank(currentTerm)) {
            	currentTerm = DateUtils.format(new Date(), DateUtils.FORMAT_DATE_YYYY_MM);
            }
            // 初始账期
            if (ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT.equals(datum.getConfigKey())
                    || ConstsSysConfig.SYS_PAYMENT_TERM_START.equals(datum.getConfigKey())) {
                datum.setConfigValue(currentTerm);
            }
        }
        boolean insertBatch = baseMapper.insertBatch(data);
        return Message.ok(insertBatch);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public Message<String> save(ConfigSys config) {
        int row = baseMapper.insert(config);
        if (row > 0) {
            return Message.ok(config.getConfigValue());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CONFIG_SYS, key = "#config.configKey")
    @Override
    public Message<String> update(ConfigSys config) {
        int row = 0;
        if (config.getConfigId() != null) {
            row = baseMapper.updateById(config);
        } else {
            row = baseMapper.update(config, new LambdaQueryWrapper<ConfigSys>()
                    .eq(ConfigSys::getConfigKey, config.getConfigKey()));
        }
        if (row > 0) {
            return Message.ok(config.getConfigValue());
        }
        throw new ServiceException("操作失败");
    }


    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void delete(ListIdsDto configIds) {
        for (String configId : configIds.getListIds()) {
            ConfigSys config = getById(configId).getData();
            if (StringUtils.equals(YesNoEnum.y.name(), config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
        }
        baseMapper.deleteByIds(Arrays.asList(configIds));
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public Boolean checkConfigKeyUnique(ConfigSys config) {
        String configId = ObjectUtil.isNull(config.getConfigId()) ? "" : config.getConfigId();
        ConfigSys info = baseMapper.selectOne(new LambdaQueryWrapper<ConfigSys>().eq(ConfigSys::getConfigKey, config.getConfigKey()));
        return !ObjectUtil.isNotNull(info) || info.getConfigId().equals(configId);
    }

    @Override
    public Message<List<ConfigSys>> getBookConfigList(String bookId) {
        List<ConfigSys> data = baseMapper.selectList(new LambdaQueryWrapper<ConfigSys>()
                .in(ConfigSys::getConfigKey, BOOKS_KEYS)
                .eq(ConfigSys::getBookId, bookId)
                .select(ConfigSys::getConfigKey, ConfigSys::getConfigValue));
        ConfigSys bookCfg = new ConfigSys();
        bookCfg.setConfigKey("bookId");
        bookCfg.setConfigValue(bookId);
        data.add(bookCfg);

        Map<String, String> maps = new HashMap<>();
        data.forEach(t -> maps.put(t.getConfigKey(), t.getConfigValue()));
        for (String booksKey : BOOKS_KEYS) {
            if (!maps.containsKey(booksKey)) {
                throw new ServiceException(String.format("账套【%1$s】缺少参数【%2$s】", bookId, booksKey));
            }
        }
        return Message.ok(data);
    }

    @Override
    public Message<List<ConfigSys>> getBookShowList(String bookId) {
        List<ConfigSys> data = baseMapper.selectList(new LambdaQueryWrapper<ConfigSys>()
                .in(ConfigSys::getConfigKey, BOOKS_SHOW_KEYS)
                .eq(ConfigSys::getBookId, bookId)
                .select(ConfigSys::getConfigKey, ConfigSys::getConfigValue));
        ConfigSys bookCfg = new ConfigSys();
        bookCfg.setConfigKey("bookId");
        bookCfg.setConfigValue(bookId);
        data.add(bookCfg);
        return Message.ok(data);
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param bookId    账套ID
     * @param configKey 参数 key
     * @return 参数值
     */
    @Override
    public String getConfigValue(String bookId, String configKey) {
        return SpringUtils.getAopProxy(this).selectConfigByKey(bookId, configKey);
    }



}
