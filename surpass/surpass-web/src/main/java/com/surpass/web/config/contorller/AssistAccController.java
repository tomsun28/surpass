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


package com.surpass.web.config.contorller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.base.vo.AssistAccVo;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.base.dto.AssistAccChangeDto;
import com.surpass.entity.base.dto.AssistAccPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.AssistAccService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 辅助核算项目管理接口
 */

@RestController
@RequestMapping("/base/assist-acc")
@Slf4j
@RequiredArgsConstructor
public class AssistAccController {
    private final AssistAccService assistAccService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<AssistAccVo>> fetch(@ParameterObject AssistAccPageDto dto,
                                            @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        if (StringUtils.isBlank(dto.getBookId())) {
            return Message.failed("所属账套ID不能为空");
        }
        return assistAccService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<AssistAccVo> getById(@PathVariable(name = "id") String id) {
        return assistAccService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody AssistAccChangeDto dto,
                                @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        return assistAccService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody AssistAccChangeDto dto,
                                  @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        return assistAccService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return assistAccService.delete(dto);
    }

}
