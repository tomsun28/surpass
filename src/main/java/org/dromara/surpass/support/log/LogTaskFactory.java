package org.dromara.surpass.support.log;


import org.dromara.surpass.dao.AuthAccountLogDao;
import org.dromara.surpass.dao.AuthOperationLogDao;
import org.dromara.surpass.pojo.entity.AuthAccountLog;
import org.dromara.surpass.pojo.entity.AuthOperationLog;
import org.dromara.surpass.support.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 *  日志操作任务工厂
 * @author tomsun28
 * @date 9:44 2018/4/22
 */
public class LogTaskFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTaskFactory.class);

    private static AuthOperationLogDao operationLogDao = SpringContextHolder.getBean(AuthOperationLogDao.class);

    private static AuthAccountLogDao accountLogDao = SpringContextHolder.getBean(AuthAccountLogDao.class);

    private LogTaskFactory() {

    }

    public static TimerTask loginLog(String username, String ip, Boolean success, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthAccountLog accountLog = LogFactory.createAccountLog(username, "用户登录日志", ip, success, message);
                    accountLogDao.save(accountLog);
                } catch (Exception e) {
                    LOGGER.error("写入用户登录日志异常", e);
                }
            }
        };
    }

    public static TimerTask exitLog(String username, String ip, Boolean succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthAccountLog accountLog = LogFactory.createAccountLog(username, "用户退出日志", ip, succeed, message);
                    accountLogDao.save(accountLog);
                } catch (Exception e) {
                    LOGGER.error("写入用户退出日志异常", e);
                }
            }
        };
    }

    public static TimerTask registerLog(String username, String ip, Boolean succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthAccountLog accountLog = LogFactory.createAccountLog(username, "用户注册日志", ip, succeed, message);
                    accountLogDao.save(accountLog);
                } catch (Exception e) {
                    LOGGER.error("写入用户注册日志异常", e);
                }
            }
        };
    }

    public static TimerTask operatorLog(String username, String resource, Boolean succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    String[] resourceTmp = resource.split("===");
                    AuthOperationLog operationLog = LogFactory.createOperationLog(username, "业务操作日志", resourceTmp[0], resourceTmp[1], succeed, message);
                    operationLogDao.save(operationLog);
                } catch (Exception e) {
                    LOGGER.error("写入业务操作日志异常", e);
                }
            }
        };
    }

    public static TimerTask exceptionLog(String username, String api, String method, Boolean succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthOperationLog exceptionLog = LogFactory.createOperationLog(username, "业务异常日志", api, method, succeed, message);
                    operationLogDao.save(exceptionLog);
                } catch (Exception e) {
                    LOGGER.error("写入业务异常日志异常");
                }
            }
        };
    }
}
