/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package org.dromara.surpass.util;

import com.jinbooks.entity.PeriodStr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/3/7 17:17
 */
public class PeriodDateUtils {
    public static PeriodStr convertToPeriod(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 设置 startPeriod
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startPeriod = calendar.getTime();

        // 设置 endPeriod
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endPeriod = calendar.getTime();

        // 1. 定义日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 2. 转换日期为字符串
        String startPeriodStr = sdf.format(startPeriod);
        String endPeriodStr = sdf.format(endPeriod);

        return new PeriodStr(startPeriodStr, endPeriodStr);
    }
}
