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

import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

/**
 * 简介说明: 渲染参数
 * <p>用于封装导出过程中的各种配置和输入数据，
 * 通过该类可灵活指定导出模板、输出目标、工作表名称等。</p>
 *
 * @author wuyan
 * {@code @date} 2025/04/23 17:46:45
 * {@code @version} 1.0
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ExcelParams<T> {

    /**
     * HTTP 响应对象，用于将生成的 Excel 推送至客户端浏览器
     */
    private HttpServletResponse httpResponse;

    /**
     * Excel 模板文件绝对路径或类路径（如 classpath:templates/report.xlsx）
     */
    private String templateFilePath;

    /**
     * 渲染模板所需的数据模型，支持单对象或列表
     */
    private T dataModel;

    /**
     * 渲染模式
     * 当且仅当dataModel类型为Object时有效
     */
    private ExcelDataModeEnum mode;

    /**
     * 导出文件名，建议包含扩展名，如 report.xlsx
     */
    private String outputFileName;

    /**
     * 本地文件保存目录，留空则不在服务器保存，仅通过 HTTP 响应输出
     */
    private String outputDirectory;

    /**
     * 目标工作表名称；若模板中不存在该名称则新建，否则使用已有
     */
    private String sheetName;

    /**
     * 是否启用相同值单元格合并（仅对列表渲染生效）
     */
    private boolean enableMergeCells;

    /**
     * 是否自动根据内容调整所有列宽
     */
    private boolean autoSizeColumns;

    /**
     * 是否在写入后强制重新计算公式
     */
    private boolean recalculateFormulas;

}
