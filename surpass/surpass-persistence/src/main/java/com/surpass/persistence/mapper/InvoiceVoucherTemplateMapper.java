package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.InvoiceVoucherTemplate;
import com.surpass.entity.invoice.dto.InvoicePageDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 24096
 */
@Mapper
public interface InvoiceVoucherTemplateMapper extends BaseMapper<InvoiceVoucherTemplate> {
    Page<InvoiceVoucherTemplate> pageList(Page page, @Param("dto") InvoiceVoucherPageDto dto);
}
