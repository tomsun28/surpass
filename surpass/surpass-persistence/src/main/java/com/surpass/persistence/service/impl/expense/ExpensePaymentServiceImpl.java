package com.surpass.persistence.service.impl.expense;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpensePayment;
import com.surpass.entity.expense.dto.ExpensePaymentChangeDto;
import com.surpass.entity.expense.dto.ExpensePaymentPageDto;
import com.surpass.entity.expense.vo.ExpensePaymentVo;
import com.surpass.persistence.mapper.ExpensePaymentMapper;
import com.surpass.persistence.service.expense.ExpensePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpensePaymentServiceImpl extends ServiceImpl<ExpensePaymentMapper, ExpensePayment> implements ExpensePaymentService {

    private final ExpensePaymentMapper baseMapper;

    @Override
    public Message<ExpensePaymentVo> queryById(String id) {
        return Message.ok(baseMapper.selectVoById(id));
    }

    @Override
    public Message<Page<ExpensePaymentVo>> pageList(ExpensePaymentPageDto dto) {
        Page<ExpensePaymentVo> pageVo = baseMapper.selectVoPageList(dto.build(), dto);
        return Message.ok(pageVo);
    }

    @Override
    public Message<String> save(ExpensePaymentChangeDto dto) {
        ExpensePayment entity = new ExpensePayment();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.insert(entity);
        return Message.ok(entity.getId());
    }

    @Override
    public Message<String> update(ExpensePaymentChangeDto dto) {
        ExpensePayment entity = new ExpensePayment();
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
