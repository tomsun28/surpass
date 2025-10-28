package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.dto.InvoiceChangeDto;
import com.surpass.entity.invoice.dto.InvoicePageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {
    Page<Invoice> pageList(Page page, @Param("dto") InvoicePageDto dto);
}
