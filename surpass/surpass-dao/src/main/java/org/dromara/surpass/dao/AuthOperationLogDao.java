package org.dromara.surpass.dao;

import org.dromara.surpass.pojo.entity.AuthOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tomsun28
 * @date 2021/3/18 22:19
 */
public interface AuthOperationLogDao extends JpaRepository<AuthOperationLog, Long> {
}
