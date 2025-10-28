package com.surpass.persistence.service.impl.expense;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseAttachment;
import com.surpass.entity.expense.dto.ExpenseAttachmentChangeDto;
import com.surpass.entity.expense.dto.ExpenseAttachmentPageDto;
import com.surpass.entity.expense.vo.ExpenseAttachmentVo;
import com.surpass.persistence.mapper.ExpenseAttachmentMapper;
import com.surpass.persistence.service.expense.ExpenseAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseAttachmentServiceImpl extends ServiceImpl<ExpenseAttachmentMapper, ExpenseAttachment> implements ExpenseAttachmentService {

    private final ExpenseAttachmentMapper baseMapper;

    @Override
    public Message<ExpenseAttachmentVo> queryById(String id) {
        return Message.ok(baseMapper.selectVoById(id));
    }

    @Override
    public Message<Page<ExpenseAttachmentVo>> pageList(ExpenseAttachmentPageDto dto) {
        LambdaQueryWrapper<ExpenseAttachment> query = new LambdaQueryWrapper<>();
        Page<ExpenseAttachmentVo> pageVo = baseMapper.selectVoPage(dto.build(), query);
        return Message.ok(pageVo);
    }

    @Override
    public Message<String> save(ExpenseAttachmentChangeDto dto) {
        ExpenseAttachment entity = new ExpenseAttachment();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.insert(entity);
        return Message.ok(entity.getId());
    }

    @Override
    public Message<String> update(ExpenseAttachmentChangeDto dto) {
        ExpenseAttachment entity = new ExpenseAttachment();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.updateById(entity);
        return Message.ok("更新成功");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        baseMapper.deleteByIds(dto.getListIds());
        return Message.ok("删除成功");
    }
}
