package org.dromara.surpass.service;


import org.dromara.surpass.pojo.entity.AuthAccountLog;
import org.dromara.surpass.pojo.entity.AuthOperationLog;
import org.springframework.data.domain.Page;


/**
 * @author tomsun28
 * @date 2021/3/18 22:22
 */
public interface LogService {


    /**
     * get account logs
     * @param currentPage current page
     * @param pageSize page size
     * @return account log
     */
    Page<AuthAccountLog> getAccountLogs(Integer currentPage, Integer pageSize);

    /**
     * get operation log
     * @param currentPage current page
     * @param pageSize page size
     * @return operation log
     */
    Page<AuthOperationLog> getOperationLogs(Integer currentPage, Integer pageSize);
}
