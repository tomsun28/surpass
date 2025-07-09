package org.dromara.surpass.dao;

import org.dromara.surpass.pojo.entity.AuthAccountLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tomsun28
 * @date 2021/3/18 22:18
 */
public interface AuthAccountLogDao extends JpaRepository<AuthAccountLog, Long> {
}
