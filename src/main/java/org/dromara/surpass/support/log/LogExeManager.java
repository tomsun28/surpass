package org.dromara.surpass.support.log;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *   日志操作任务运行管理器 单例
 * @author tomsun28
 * @date 9:37 2018/4/22
 */
public class LogExeManager {

    private static final int LOG_DELAY_TIME = 10;

    private ThreadFactory threadFactory = new CustomizableThreadFactory("log-pool-");

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(20, threadFactory);

    private static LogExeManager logExeManager = new LogExeManager();
    private LogExeManager() {}

    public static LogExeManager getInstance() {
        return logExeManager;
    }

    public void executeLogTask(TimerTask timerTask) {
        executor.schedule(timerTask, LOG_DELAY_TIME, TimeUnit.MICROSECONDS);
    }
}
