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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigPersonalTax;
import com.surpass.entity.config.dto.ConfigPersonalTaxChangeDto;
import com.surpass.entity.config.dto.ConfigPersonalTaxPageDto;
import com.surpass.entity.dto.ListIdsDto;

public interface ConfigPersonalTaxService extends IService<ConfigPersonalTax> {
    Message<Page<ConfigPersonalTax>> pageList(ConfigPersonalTaxPageDto dto);

    Message<String> save(ConfigPersonalTaxChangeDto dto);

    Message<String> update(ConfigPersonalTaxChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
