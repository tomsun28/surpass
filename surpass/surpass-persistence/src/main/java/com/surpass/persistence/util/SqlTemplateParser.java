package com.surpass.persistence.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SqlTemplateParser {

    public ParsedSql parseSqlTemplate(String sqlTemplate, Map<String, Object> params) {
        List<String> paramNames = new ArrayList<>();
        StringBuilder parsedSql = new StringBuilder();

        int startIndex = 0;
        int paramStart;

        while ((paramStart = sqlTemplate.indexOf("#{", startIndex)) != -1) {
            int paramEnd = sqlTemplate.indexOf("}", paramStart);
            if (paramEnd == -1) {
                throw new RuntimeException("SQL模板格式错误，缺少闭合的}");
            }

            // 添加SQL片段
            parsedSql.append(sqlTemplate, startIndex, paramStart);

            // 提取参数名
            String paramName = sqlTemplate.substring(paramStart + 2, paramEnd);

            // 检查参数是否存在
            if (!params.containsKey(paramName)) {
                throw new RuntimeException("缺少必要参数: " + paramName);
            }

            // 添加参数占位符
            parsedSql.append("?");
            paramNames.add(paramName);

            startIndex = paramEnd + 1;
        }

        // 添加剩余的SQL片段
        if (startIndex < sqlTemplate.length()) {
            parsedSql.append(sqlTemplate.substring(startIndex));
        }

        return new ParsedSql(parsedSql.toString(), paramNames);
    }

    public List<String> extractParamNames(String sqlTemplate) {
        List<String> paramNames = new ArrayList<>();

        int startIndex = 0;
        int paramStart;

        while ((paramStart = sqlTemplate.indexOf("#{", startIndex)) != -1) {
            int paramEnd = sqlTemplate.indexOf("}", paramStart);
            if (paramEnd == -1) {
                throw new RuntimeException("SQL模板格式错误，缺少闭合的}");
            }

            String paramName = sqlTemplate.substring(paramStart + 2, paramEnd);
            paramNames.add(paramName);

            startIndex = paramEnd + 1;
        }

        return paramNames;
    }

    public static class ParsedSql {
        private final String sql;
        private final List<String> paramNames;

        public ParsedSql(String sql, List<String> paramNames) {
            this.sql = sql;
            this.paramNames = paramNames;
        }

        public String getSql() {
            return sql;
        }

        public List<String> getParamNames() {
            return paramNames;
        }
    }
}
