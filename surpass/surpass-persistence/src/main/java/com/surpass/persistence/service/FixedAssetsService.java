package com.surpass.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.FixedAssetsChangeDto;
import com.surpass.entity.fa.dto.FixedAssetsPageDto;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;

public interface FixedAssetsService extends IService<FixedAssets> {
    Message<Page<FixedAssets>> pageList(FixedAssetsPageDto dto);

    Message<FixedAssets> getById(String id);

    Message<String> save(FixedAssetsChangeDto dto);

    Message<String> update(FixedAssetsChangeDto dto);

    Message<String> delete(ListIdsDto dto);

    Message<String> generateVoucher(GenerateVoucherDto dto);

    Message<String> deleteVoucher(GenerateVoucherDto dto);
}
