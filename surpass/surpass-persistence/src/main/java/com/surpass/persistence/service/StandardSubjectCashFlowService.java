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

import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.standard.StandardSubjectCashFlow;
import com.surpass.entity.standard.dto.StandardSubjectCashFlowDto;
import com.surpass.entity.standard.vo.StandardSubjectCashFlowVo;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/4/18 10:32
 */

public interface StandardSubjectCashFlowService extends IService<StandardSubjectCashFlow> {
    Message<String> save(StandardSubjectCashFlowDto dto);

    Message<StandardSubjectCashFlowVo> fetchRelationships(StandardSubjectCashFlowDto dto);

    boolean saveTemplateRelationships(String bookId);

    boolean deleteByBookIds(List<String> bookIds);
}
