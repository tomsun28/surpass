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


package org.dromara.surpass.util.excel;

import com.jinbooks.enums.BaseEnum;
import com.jinbooks.util.DateUtils;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ExcelExportCfg {
    /**
     * Excel 列名（表头）
     */
    String name() default "";

    /**
     * 列排序，值越小越靠前
     */
    int order() default Integer.MAX_VALUE;

    /**
     * 列宽
     */
    int width() default 20;

    /**
     * 日期格式，仅适用于 Date 类型
     */
    String dateFormat() default DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS;

    /**
     * 数字格式，如 "#.##"
     */
    String numberFormat() default "#.##";

    /**
     * 0值处理格式，如 ""
     */
    String numberZeroFormat() default "";

    /**
     * 是否忽略该字段
     */
    boolean ignore() default false;

    /**
     * 是否换行显示
     */
    boolean wrapText() default false;

    /**
     * 是否自动合并单元格
     */
    boolean mergeCells() default false;

    /**
     * 映射为中文（如枚举类型）
     * 枚举类必须实现 BaseEnum 接口
     */
    Class<? extends BaseEnum> enumClass() default BaseEnum.Default.class;
}
