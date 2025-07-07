package org.dromara.surpass.support.log;

import org.dromara.surpass.pojo.entity.AuthAccountLog;
import org.dromara.surpass.pojo.entity.AuthOperationLog;

/**
 *   日志对象工厂
 * @author tomsun28
 * @date 9:50 2018/4/22
 */
public class LogFactory {

    private LogFactory() {}

    public static AuthAccountLog createAccountLog(String username, String logName, String ip, Boolean success, String message) {
        AuthAccountLog accountLog = new AuthAccountLog();
        accountLog.setUsername(username);
        accountLog.setLogName(logName);
        accountLog.setRemoteIp(ip);
        accountLog.setSuccess(success);
        accountLog.setMessage(message);
        return accountLog;
    }

    public static AuthOperationLog createOperationLog(String username, String logName, String api, String method, Boolean success, String message) {
        AuthOperationLog operationLog = new AuthOperationLog();
        operationLog.setUsername(username);
        operationLog.setLogName(logName);
        operationLog.setApi(api);
        operationLog.setMethod(method);
        operationLog.setSuccess(success);
        operationLog.setMessage(message);
        return operationLog;
    }
}
