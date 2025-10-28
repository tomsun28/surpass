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

import com.surpass.entity.Message;
import com.surpass.entity.PageQuery;
import com.surpass.entity.config.ConfigSys;
import com.surpass.entity.dto.ListIdsDto;

import java.util.Date;
import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author Wuyan
 */
public interface ConfigSysService {
    Message<List<ConfigSys>> pageList(ConfigSys config, PageQuery pageQuery);

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    Message<ConfigSys> getById(String configId);

    /**
     * 根据键名查询参数配置信息
     *
     * @param bookId 账套ID
     * @param configKey 参数键名
     * @return 参数键值
     */
    String selectConfigByKey(String bookId, String configKey);

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    Message<List<ConfigSys>> selectConfigList(ConfigSys config);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    Message<String> save(ConfigSys config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    Message<String> update(ConfigSys config);

    public Message<String> updateCurrentTerm(String bookId,String currentTerm);

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    void delete(ListIdsDto configIds);

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    Boolean checkConfigKeyUnique(ConfigSys config);

    Message<List<ConfigSys>> getBookConfigList(String bookId);

    /**
     * 账套的当前期
     * @param bookId
     * @return
     */
    String getCurrentTerm(String bookId);

    /**
     * 账套的当前期的月末日期
     * @param bookId
     * @return
     */
    Date getCurrentTermLastDate(String bookId);

    String getCurrentTermLastDateString(String bookId);

    /**
     * 账套上一期
     * @param bookId
     * @return
     */
    public String getPrevTerm(String bookId);

    /**
     * 账套下一期
     * @param bookId
     * @return
     */
    String getNextTerm(String bookId);

    /**
     * 账套转到下一期
     * @param bookId
     * @return
     */
    public Message<String> termToNext(String bookId);

    /**
     * 账套初始期
     * @param bookId
     * @return
     */
    String getTermStart(String bookId);

    Message<List<ConfigSys>> getBookShowList(String bookId);

    Message<Boolean> initBooksConfig(String bookId,String currentTerm);
}
