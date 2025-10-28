package com.surpass.persistence.service.impl.expense;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.ExpenseReport;
import com.surpass.entity.expense.dto.ExpenseDetailChangeDto;
import com.surpass.entity.expense.dto.ExpenseDetailPageDto;
import com.surpass.entity.expense.dto.ExpenseReportChangeDto;
import com.surpass.entity.expense.dto.ExpenseReportPageDto;
import com.surpass.entity.expense.vo.ExpenseDetailVo;
import com.surpass.entity.expense.vo.ExpenseReportVo;
import com.surpass.persistence.mapper.ExpenseDetailMapper;
import com.surpass.persistence.mapper.ExpenseReportMapper;
import com.surpass.persistence.service.expense.ExpenseDetailService;
import com.surpass.persistence.service.expense.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExpenseReportServiceImpl extends ServiceImpl<ExpenseReportMapper, ExpenseReport> implements ExpenseReportService {

    private final ExpenseReportMapper baseMapper;
    private final ExpenseDetailService expenseDetailService;
    private final ExpenseDetailMapper expenseDetailMapper;

    @Override
    public Message<ExpenseReportVo> queryById(String id) {
        ExpenseReportVo expenseReportVo = baseMapper.selectVoById(id);
        if (expenseReportVo == null) {
            return Message.failed("查询对象不存在");
        }
        ExpenseDetailPageDto expenseDetailPageDto = new ExpenseDetailPageDto();
        expenseDetailPageDto.setReportId(id);
        expenseDetailPageDto.setPageSize(Integer.MAX_VALUE);
        List<ExpenseDetailVo> records = expenseDetailService.pageList(expenseDetailPageDto).getData().getRecords();
        expenseReportVo.setItems(records);
        return Message.ok(expenseReportVo);
    }

    @Override
    public Message<Page<ExpenseReportVo>> pageList(ExpenseReportPageDto dto) {
        Page<ExpenseReportVo> pageVo = baseMapper.selectVoPageList(dto.build(), dto);
        return Message.ok(pageVo);
    }

    @Override
    @Transactional
    public Message<String> save(ExpenseReportChangeDto dto) {
        ExpenseReport entity = new ExpenseReport();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.insert(entity);

        List<ExpenseDetailChangeDto> items = dto.getItems();
        for (ExpenseDetailChangeDto item : items) {
            item.setReportId(entity.getId());
            item.setBookId(dto.getBookId());
            expenseDetailService.save(item);
        }

        return Message.ok(entity.getId());
    }

    @Override
    @Transactional
    public Message<String> update(ExpenseReportChangeDto dto) {
        ExpenseReport entity = new ExpenseReport();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.updateById(entity);

        List<ExpenseDetailChangeDto> items = dto.getItems();

        deleteExpenseDetail(entity.getId());

        for (ExpenseDetailChangeDto item : items) {
            item.setReportId(entity.getId());
            item.setBookId(dto.getBookId());
            item.setId(null);
            expenseDetailService.save(item);
        }

        return Message.ok("更新成功");
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        baseMapper.deleteByIds(dto.getListIds());
        for (String listId : dto.getListIds()) {
            deleteExpenseDetail(listId);
        }
        return Message.ok("删除成功");
    }

    private void deleteExpenseDetail(String reportId) {
        ExpenseDetailPageDto expenseDetailPageDto = new ExpenseDetailPageDto();
        expenseDetailPageDto.setReportId(reportId);
        expenseDetailPageDto.setPageSize(Integer.MAX_VALUE);
        List<ExpenseDetailVo> records = expenseDetailService.pageList(expenseDetailPageDto).getData().getRecords();
        for (ExpenseDetailVo record : records) {
            expenseDetailMapper.deleteById(record.getId());
        }
    }
}
