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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.base.AssistAcc;
import com.surpass.entity.base.vo.AssistAccVo;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.base.dto.AssistAccChangeDto;
import com.surpass.entity.base.dto.AssistAccPageDto;
import com.surpass.exception.ServiceException;
import com.surpass.persistence.mapper.AssistAccMapper;
import com.surpass.persistence.service.AssistAccService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 辅助核算项目Service业务层处理
 *
 * @author wuyan
 * {@code @date} 2025-02-18
 */
@RequiredArgsConstructor
@Service
public class AssistAccServiceImpl extends ServiceImpl<AssistAccMapper, AssistAcc> implements AssistAccService {

    private final AssistAccMapper assistAccMapper;
    private final IdentifierGenerator identifierGenerator;

    @Override
    public Message<AssistAccVo> getById(String id) {
        return Message.ok(assistAccMapper.selectVoById(id));
    }

    /**
     * 分页查询
     *
     * @param dto 分页参数
     * @return 查询结果
     */
    @Override
    public Message<Page<AssistAccVo>> pageList(AssistAccPageDto dto) {
        LambdaQueryWrapper<AssistAcc> lqw = buildQueryWrapper(dto);
        Page<AssistAccVo> result = assistAccMapper.selectVoPage(dto.build(), lqw);
        return Message.ok(result);
    }

    /**
     * 插入数据
     *
     * @param dto 插入对象
     * @return 插入结果
     */
    @Override
    @Transactional
    public Message<String> save(AssistAccChangeDto dto) {
        AssistAcc assistAcc = AssistAcc.builder().build();
        BeanUtil.copyProperties(dto, assistAcc);
        validEntityBeforeSave(assistAcc);
        String currentId = identifierGenerator.nextId(assistAcc).toString();
        assistAcc.setId(currentId);
        boolean save = super.save(assistAcc);
        return save ? new Message<>(Message.SUCCESS, "新增成功", currentId) : new Message<>(Message.FAIL, "新增失败");
    }

    /**
     * 更新信息
     *
     * @param dto 更新对象
     * @return 结果
     */
    @Override
    @Transactional
    public Message<String> update(AssistAccChangeDto dto) {
        AssistAcc assistAcc = AssistAcc.builder().build();
        BeanUtil.copyProperties(dto, assistAcc);
        validEntityBeforeSave(assistAcc);
        String currentId = dto.getId();
        boolean update = super.updateById(assistAcc);
        return update ? new Message<>(Message.SUCCESS, "修改成功", currentId) : new Message<>(Message.FAIL, "修改失败");
    }


    /**
     * 根据ID删除
     *
     * @param dto ID组
     * @return 结果
     */
    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> ids = dto.getListIds();
        boolean result = super.removeBatchByIds(ids);
        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    /**
     * 构建查询条件
     *
     * @param bo 查询参数
     */
    private LambdaQueryWrapper<AssistAcc> buildQueryWrapper(AssistAccPageDto bo) {
        LambdaQueryWrapper<AssistAcc> lqw = Wrappers.lambdaQuery();
        lqw.ne(StringUtils.isNotBlank(bo.getNoId()), AssistAcc::getId, bo.getNoId());
        lqw.eq(StringUtils.isNotBlank(bo.getBookId()), AssistAcc::getBookId, bo.getBookId());
        lqw.eq(StringUtils.isNotBlank(bo.getAssistType()), AssistAcc::getAssistType, bo.getAssistType());
        lqw.eq(StringUtils.isNotBlank(bo.getAssistCode()), AssistAcc::getAssistCode, bo.getAssistCode());
        lqw.likeLeft(StringUtils.isNotBlank(bo.getAssistName()), AssistAcc::getAssistName, bo.getAssistName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), AssistAcc::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AssistAcc entity) {
        AssistAccPageDto dto = AssistAccPageDto.builder()
                .noId(entity.getId())
                .bookId(entity.getBookId())
                .assistCode(entity.getAssistCode())
                .assistType(entity.getAssistType())
                .build();
        LambdaQueryWrapper<AssistAcc> lqw = buildQueryWrapper(dto);
        AssistAcc assistAcc = assistAccMapper.selectOne(lqw);
        if (assistAcc != null) {
            throw new ServiceException("辅助核算项目编码重复");
        }
    }
}
