package com.surpass.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.fa.FaDepreciationRecord;
import com.surpass.entity.fa.dto.DepreciationRecordPageDto;
import com.surpass.entity.fa.dto.ProvisionDto;

import java.util.Map;

public interface FaDepreciationRecordService extends IService<FaDepreciationRecord> {
    Message<Map<String, Object>> pageList(DepreciationRecordPageDto dto);

    Message<String> generateProvision(ProvisionDto dto);
}
