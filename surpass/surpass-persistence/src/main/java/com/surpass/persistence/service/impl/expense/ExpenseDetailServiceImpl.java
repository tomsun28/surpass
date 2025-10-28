package com.surpass.persistence.service.impl.expense;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseDetail;
import com.surpass.entity.expense.dto.ExpenseDetailChangeDto;
import com.surpass.entity.expense.dto.ExpenseDetailPageDto;
import com.surpass.entity.expense.vo.ExpenseDetailVo;
import com.surpass.persistence.mapper.ExpenseDetailMapper;
import com.surpass.persistence.service.expense.ExpenseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseDetailServiceImpl extends ServiceImpl<ExpenseDetailMapper, ExpenseDetail> implements ExpenseDetailService {

    private final ExpenseDetailMapper baseMapper;

    @Override
    public Message<ExpenseDetailVo> queryById(String id) {
        return Message.ok(baseMapper.selectVoById(id));
    }

    @Override
    public Message<Page<ExpenseDetailVo>> pageList(ExpenseDetailPageDto dto) {
        LambdaQueryWrapper<ExpenseDetail> query = new LambdaQueryWrapper<>();
        Page<ExpenseDetailVo> pageVo = baseMapper.selectVoPage(dto.build(), query);
        return Message.ok(pageVo);
    }

    @Override
    public Message<String> save(ExpenseDetailChangeDto dto) {
        ExpenseDetail entity = new ExpenseDetail();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.insert(entity);
        return Message.ok(entity.getId());
    }

    @Override
    public Message<String> update(ExpenseDetailChangeDto dto) {
        ExpenseDetail entity = new ExpenseDetail();
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
