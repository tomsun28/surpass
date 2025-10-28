package com.surpass.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.surpass.entity.invoice.InvoiceItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceItemMapper extends BaseMapper<InvoiceItem> {
}
