package com.surpass.persistence.service.impl.expense;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseApproval;
import com.surpass.entity.expense.dto.ExpenseApprovalChangeDto;
import com.surpass.entity.expense.dto.ExpenseApprovalPageDto;
import com.surpass.entity.expense.vo.ExpenseApprovalVo;
import com.surpass.persistence.mapper.ExpenseApprovalMapper;
import com.surpass.persistence.service.expense.ExpenseApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseApprovalServiceImpl extends ServiceImpl<ExpenseApprovalMapper, ExpenseApproval> implements ExpenseApprovalService {

    private final ExpenseApprovalMapper baseMapper;

    @Override
    public Message<ExpenseApprovalVo> queryById(String id) {
        return Message.ok(baseMapper.selectVoById(id));
    }

    @Override
    public Message<Page<ExpenseApprovalVo>> pageList(ExpenseApprovalPageDto dto) {
        LambdaQueryWrapper<ExpenseApproval> query = new LambdaQueryWrapper<>();
        Page<ExpenseApprovalVo> pageVo = baseMapper.selectVoPage(dto.build(), query);
        return Message.ok(pageVo);
    }

    @Override
    public Message<String> save(ExpenseApprovalChangeDto dto) {
        ExpenseApproval entity = new ExpenseApproval();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.insert(entity);
        return Message.ok(entity.getId());
    }

    @Override
    public Message<String> update(ExpenseApprovalChangeDto dto) {
        ExpenseApproval entity = new ExpenseApproval();
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
