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

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.book.dto.SettlementPageDto;
import com.surpass.entity.book.vo.SettlementVerifyVo;

public interface SettlementService extends IService<Settlement> {

    Message<Page<Settlement>> pageList(SettlementPageDto dto);

    Message<Settlement> checkout(Settlement dto);

    /**
     * 判断账期情况
     * @param period YYYY-MM格式
     * @return
     */
    Message<String> check(String bookId,String period);

    /**
     * 当前账期结账验证
     * @param bookId
     * @return
     */
    Message<List<SettlementVerifyVo>> verify(String bookId);

}
