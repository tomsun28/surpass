package com.surpass.web.historys.contorller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.history.HistoryOpenapi;
import com.surpass.entity.history.HistorySystemLogs;
import com.surpass.entity.history.dto.HistoryOpenapiPageDto;
import com.surpass.entity.history.dto.HistorySystemLogsPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.HistoryOpenapiService;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/30 9:55
 */

@RestController
@RequestMapping(value={"/historys"})
public class HistoryOpenapiController {
    static final Logger logger = LoggerFactory.getLogger(HistoryOpenapiController.class);

    @Autowired
    HistoryOpenapiService historyOpenapiService;

    /**
     * 查询OpenApi日志
     * @param dto
     * @return
     */
    @GetMapping(value={"/openapi/fetch"})
    @ResponseBody
    public Message<JpaPageResults<HistoryOpenapi>> fetch(@ParameterObject HistoryOpenapiPageDto dto,
                                                         @CurrentUser UserInfo currentUser){
        logger.debug("historys/openapi/fetch {} ",dto);

        return historyOpenapiService.page(dto);
    }
}
