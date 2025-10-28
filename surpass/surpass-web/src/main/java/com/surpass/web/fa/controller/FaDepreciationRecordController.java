package com.surpass.web.fa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.fa.FaDepreciationRecord;
import com.surpass.entity.fa.dto.DepreciationRecordPageDto;
import com.surpass.entity.fa.dto.ProvisionDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.FaDepreciationRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/9 10:59
 */

@RestController
@RequestMapping("/fa/depreciation")
@Slf4j
@RequiredArgsConstructor
public class FaDepreciationRecordController {
    private final FaDepreciationRecordService depreciationRecordService;

    @GetMapping(value = {"/fetch"})
    public Message<Map<String, Object>> fetch(@ParameterObject DepreciationRecordPageDto dto,
                                                    @CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setBookId(currentUser.getBookId());
        return depreciationRecordService.pageList(dto);
    }

    @PostMapping("/provision")
    public Message<String> generateProvision(@RequestBody ProvisionDto dto,
                                                   @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return depreciationRecordService.generateProvision(dto);
    }
}
