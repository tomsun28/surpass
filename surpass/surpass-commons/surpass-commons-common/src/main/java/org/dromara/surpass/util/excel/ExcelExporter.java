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
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dromara.surpass.enums.BaseEnum;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简介说明: excel 导出工具包
 * 支持读取 .xlsx 模板；
 * 支持 ${属性} 占位符替换；
 * 支持嵌套属性如 ${item.name}；
 * 支持列表（数组）渲染为多行；
 * 输出到 HttpServletResponse。
 *
 * @author wuyan
 * {@code @date} 2025/04/23 14:48:29
 * {@code @version} 1.0
 */

@Slf4j
public class ExcelExporter {
    /**
     * 输出 Content-Type
     */
    public static final String APPLICATION_MS_EXCEL =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8";

    /**
     * 占位符正则：${property.path}
     */
    private static final Pattern PLACEHOLDER_PATTERN =
            Pattern.compile("\\$\\{\\s*([^\\}]+?)\\s*\\}");

    /**
     * POI 格式化器，用于读取任意类型单元格的文本
     */
    private static final DataFormatter FORMATTER = new DataFormatter();

    /**
     * 按配置执行导出（模板渲染、文件保存、HTTP 输出等）
     *
     * @param params 导出参数，详见 {@link ExcelParams}
     */
    public static <T> void export(ExcelParams<T> params) throws IOException {
        Objects.requireNonNull(params, "ExcelParams 不能为 null");

        try (InputStream is = Files.newInputStream(Paths.get(params.getTemplateFilePath()));
             Workbook wb = WorkbookFactory.create(is)) {

            Sheet sheet = obtainSheet(wb, params.getSheetName());
            Object model = params.getDataModel();

            // 渲染
            if (model instanceof Collection<?>) {
                renderList(sheet, (Collection<?>) model, params.isEnableMergeCells());
            } else {
                renderObject(sheet, model, params);
            }

            // 列宽自适应
            if (params.isAutoSizeColumns()) {
                int lastCol = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();
                for (int c = 0; c < lastCol; c++) sheet.autoSizeColumn(c);
            }
            // 强制公式计算
            if (params.isRecalculateFormulas()) {
                wb.setForceFormulaRecalculation(true);
            }

            // 保存到本地
            if (params.getOutputDirectory() != null && !params.getOutputDirectory().isEmpty()) {
                File dir = new File(params.getOutputDirectory());
                if (!dir.exists()) dir.mkdirs();
                File outFile = new File(dir, params.getOutputFileName());
                try (OutputStream fos = new FileOutputStream(outFile)) {
                    wb.write(fos);
                }
            }

            // 输出到 HTTP
            if (params.getHttpResponse() != null) {
                HttpServletResponse resp = params.getHttpResponse();
                resp.setContentType(APPLICATION_MS_EXCEL);
                resp.setHeader("Content-Disposition",
                        "attachment; filename=\"" + params.getOutputFileName() + "\"");
                try (OutputStream os = resp.getOutputStream()) {
                    wb.write(os);
                    os.flush();
                }
            }
        }
    }

    /**
     * 获取或创建指定 Sheet；若名称为空使用第一个
     */
    private static Sheet obtainSheet(Workbook wb, String sheetName) {
        if (sheetName != null && !sheetName.isEmpty()) {
            Sheet sh = wb.getSheet(sheetName);
            return (sh != null) ? sh : wb.createSheet(sheetName);
        }
        return wb.getSheetAt(0);
    }

    /**
     * 渲染单对象：替换所有字符串单元格中的 ${...} 占位符
     */
    private static <T> void renderObject(Sheet sheet, Object data, ExcelParams<T> params) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.STRING) continue;
                String txt = cell.getStringCellValue();
                Matcher m = PLACEHOLDER_PATTERN.matcher(txt);
                StringBuilder sb = new StringBuilder();
                while (m.find()) {
                    Object val = getNestedProperty(data, m.group(1));
                    // 列表不替换
                    if (val == null
                            || (val instanceof Collection && params.getMode().equals(ExcelDataModeEnum.base_attribute))) {
                        continue;
                    }
                    m.appendReplacement(sb, Matcher.quoteReplacement(
                            val != null ? val.toString() : ""));
                }
                m.appendTail(sb);
                cell.setCellValue(sb.toString());
            }
        }
    }

    /**
     * 渲染列表：按模板行动态插入多行并替换占位符，可合并单元格
     */
    private static void renderList(Sheet sheet,
                                   Collection<?> items,
                                   boolean enableMerge) {
        // 查找模板行、记录路径与样式
        int tplR = -1;
        List<String> paths = new ArrayList<>();
        List<String> leftTexts = new ArrayList<>();
        List<String> rightTexts = new ArrayList<>();
        List<CellStyle> cellStyles = new ArrayList<>();
        short rowHt = 0;
        CellStyle rowStyle = null;
        for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            List<String> tmpP = new ArrayList<>();
            List<CellStyle> tmpS = new ArrayList<>();
            List<String> tmpL = new ArrayList<>();
            List<String> tmpR = new ArrayList<>();
            boolean found = false;
            for (Cell cell : row) {
                String txt = FORMATTER.formatCellValue(cell);
                Matcher m = PLACEHOLDER_PATTERN.matcher(txt);
                if (m.find()) {
                    String full = m.group();           // 例如 ${wuyan}
                    String name = m.group(1).trim();   // 提取 wuyan
                    int start = txt.indexOf(full);
                    int end = start + full.length();
                    String left = txt.substring(0, start);
                    String right = txt.substring(end);
                    tmpP.add(name);
                    tmpL.add(left);
                    tmpR.add(right);
                    found = true;
                } else {
                    tmpP.add(null);
                    tmpL.add(txt);
                    tmpR.add("");
                }
                tmpS.add(cell.getCellStyle());
            }
            if (found) {
                tplR = r;
                paths = tmpP;
                leftTexts = tmpL;
                rightTexts = tmpR;
                cellStyles = tmpS;
                rowHt = row.getHeight();
                rowStyle = row.getRowStyle();
                break;
            }
        }
        if (tplR < 0) throw new IllegalArgumentException("模板行未找到");

        int writeR = tplR;
        for (Object it : items) {
            Map<Integer, List<Object>> colVals = new HashMap<>();
            int max = 1;
            for (int c = 0; c < paths.size(); c++) {
                String p = paths.get(c);
                if (p == null) continue;
                Object raw = getNestedProperty(it, p);
                if (raw == null) raw = "";
                List<Object> vals = raw instanceof Collection ?
                        new ArrayList<>((Collection<?>) raw) :
                        Collections.singletonList(raw);
                colVals.put(c, vals);
                max = Math.max(max, vals.size());
            }
            int start = writeR;
            for (int i = 0; i < max; i++, writeR++) {
                Row nrow = sheet.createRow(writeR);
                nrow.setHeight(rowHt);
                if (rowStyle != null) nrow.setRowStyle(rowStyle);
                for (int c = 0; c < paths.size(); c++) {
                    Cell ncell = nrow.createCell(c);
                    ncell.setCellStyle(cellStyles.get(c));
                    List<Object> vs = colVals.get(c);
                    if (vs == null) {
                        if (leftTexts.get(c) != null) {
                            ncell.setCellValue(leftTexts.get(c));
                        }
                        continue;
                    }
                    Object v = (vs.size() == 1 ? vs.get(0) : (i < vs.size() ? vs.get(i) : null));
                    if (v != null) {
                        ncell.setCellValue(leftTexts.get(c) + v + rightTexts.get(c));
                    }
                }
            }
            if (enableMerge && writeR - start > 1) {
                int finalWriteR = writeR;
                colVals.forEach((c, vs) -> {
                    if (vs.size() == 1) {
                        sheet.addMergedRegion(new CellRangeAddress(start, finalWriteR - 1, c, c));
                    }
                });
            }
        }
    }

    /**
     * 递归解析嵌套属性，自动扁平化 Collection
     */
    private static Object getNestedProperty(Object obj, String path) {
        String[] parts = path.split("\\.");
        return extract(obj, parts, 0);
    }

    private static Object extract(Object cur, String[] parts, int idx) {
        if (cur == null || idx >= parts.length) return cur;
        String prop = parts[idx];
        Object next = invokeGetter(cur, prop);
        if (next == null) return null;
        if (next instanceof Collection) {
            List<Object> acc = new ArrayList<>();
            for (Object e : (Collection<?>) next) {
                Object x = extract(e, parts, idx + 1);
                if (x instanceof Collection) acc.addAll((Collection<?>) x);
                else acc.add(x);
            }
            return acc;
        } else {
            next = convertValueIfNeeded(cur, prop, next);
        }
        return extract(next, parts, idx + 1);
    }

    /**
     * 数据格式转换映射
     *
     * @param obj      对象
     * @param prop     字段名
     * @param rawValue 字段值
     * @return 枚举值
     */
    private static Object convertValueIfNeeded(Object obj, String prop, Object rawValue) {
        try {
            Field field = getFieldRecursive(obj.getClass(), prop);
            ExcelExportCfg cfg = field.getAnnotation(ExcelExportCfg.class);
            if (cfg != null) {
                // 对于BigDecimal类型，如果值为0，则转换为""
                if (rawValue instanceof BigDecimal value) {
                    if (value.compareTo(BigDecimal.ZERO) == 0) {
                        return cfg.numberZeroFormat();
                    }
                }

                // 枚举映射处理
                if (cfg.enumClass() != BaseEnum.Default.class) {
                    BaseEnum enumInstance = cfg.enumClass().getEnumConstants()[0];
                    Map<Object, Object> map = enumInstance.getMap();
                    if (rawValue != null && map.containsKey(rawValue)) {
                        return map.get(rawValue);
                    }
                }
                // 日期格式化处理
                if (!cfg.dateFormat().isEmpty() && rawValue instanceof Date) {
                    return new SimpleDateFormat(cfg.dateFormat()).format((Date) rawValue);
                }
                // 数字格式化处理
                if (!cfg.numberFormat().isEmpty() && rawValue instanceof Number) {
                    DecimalFormat df = new DecimalFormat(cfg.numberFormat());
                    return df.format(rawValue);
                }
            }
        } catch (NoSuchFieldException e) {
            log.warn("字段未找到：{} {}", prop, e.getMessage());
        } catch (Exception e) {
            log.warn("枚举映射失败：{} {}", prop, e.getMessage());
        }
        return rawValue;
    }

    private static Field getFieldRecursive(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // 继续在父类中查找
            }
        }
        throw new NoSuchFieldException("字段未找到：" + fieldName);
    }

    /**
     * 通过反射调用getter方法获取属性值，如果是Map，则直接返回对应的值
     *
     * @param obj  对象
     * @param prop 参数名
     */
    private static Object invokeGetter(Object obj, String prop) {
        try {
            if (obj instanceof Map<?, ?>) {
                return ((Map<?, ?>) obj).get(prop);
            }

            String name = "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
            Method m;
            try {
                m = obj.getClass().getMethod(name);
            } catch (NoSuchMethodException e) {
                m = obj.getClass().getMethod("is" + name.substring(3));
            }
            return m.invoke(obj);
        } catch (Exception e) {
            try {
                Field f = obj.getClass().getDeclaredField(prop);
                f.setAccessible(true);
                return f.get(obj);
            } catch (Exception ex) {
                log.warn("字段未找到：{}, {}", prop, ex.getMessage());
                return null;
            }
        }
    }

}
