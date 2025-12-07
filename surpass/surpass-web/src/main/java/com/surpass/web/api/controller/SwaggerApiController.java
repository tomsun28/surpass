package com.surpass.web.api.controller;

import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.ApiVersion;
import com.surpass.persistence.service.ApiDefinitionService;
import com.surpass.persistence.service.ApiVersionService;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 动态API文档控制器，提供符合SpringDoc规范的API文档接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api-docs")
public class SwaggerApiController {

    private final ApiDefinitionService apiDefinitionService;
    private final ApiVersionService apiVersionService;

    /**
     * 返回所有已发布动态API的OpenAPI文档定义
     *
     * @return OpenAPI文档对象
     */
    @GetMapping(value = "/dynamic-apis")
    public OpenAPI getDynamicApiDocs(HttpServletRequest request) {
        OpenAPI openAPI = new OpenAPI();
        
        // 设置OpenAPI版本
        openAPI.setOpenapi("3.0.3");

        // 设置基本信息
        Info info = new Info()
                .title("动态API文档")
                .version("1.0.0")
                .description("由系统动态生成的API文档");
        openAPI.setInfo(info);

        // 添加服务器信息
        List<Server> servers = new ArrayList<>();
        Server server = new Server();
        server.setUrl("/");
        server.setDescription("当前服务器");
        servers.add(server);
        openAPI.setServers(servers);

        // 设置组件（安全方案等）
        Components components = new Components();
        // 添加Bearer Token安全方案
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT认证令牌");
        components.addSecuritySchemes("bearerAuth", bearerAuth);
        
        // 添加Basic Auth安全方案
        SecurityScheme basicAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("基础认证");
        components.addSecuritySchemes("basicAuth", basicAuth);
        
        openAPI.setComponents(components);

        // 添加全局安全要求
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");
        securityRequirements.add(securityRequirement);
        openAPI.setSecurity(securityRequirements);

        // 添加标签
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag().name("动态API").description("系统动态生成的API"));
        openAPI.setTags(tags);

        // 构建路径信息
        Paths paths = new Paths();

        // 获取所有API定义
        List<ApiDefinition> apiDefinitions = apiDefinitionService.findAll();

        // 遍历每个API定义，查找其已发布的版本
        for (ApiDefinition apiDefinition : apiDefinitions) {
            // 获取已发布的版本
            ApiVersion publishedVersion = apiVersionService.findPublishedVersionByApiId(apiDefinition.getId());

            if (publishedVersion != null) {
                // 根据API定义构建路径
                io.swagger.v3.oas.models.PathItem pathItem = new io.swagger.v3.oas.models.PathItem();

                // 从路径中提取路径参数
                List<Parameter> pathParameters = extractPathParameters(apiDefinition.getPath());
                
                // 构建查询参数
                List<Parameter> queryParameters = buildQueryParameters(publishedVersion);
                
                // 合并所有参数
                List<Parameter> allParameters = new ArrayList<>();
                allParameters.addAll(pathParameters);
                allParameters.addAll(queryParameters);

                // 构建响应
                ApiResponses apiResponses = buildResponses(publishedVersion);

                // 根据HTTP方法设置对应的操作
                io.swagger.v3.oas.models.Operation operation = new io.swagger.v3.oas.models.Operation();
                operation.setOperationId(apiDefinition.getId() + "_" + apiDefinition.getMethod().toLowerCase());
                operation.setSummary(apiDefinition.getName());
                operation.setDescription(apiDefinition.getDescription());
                operation.setParameters(allParameters);
                operation.setResponses(apiResponses);
                operation.addTagsItem("动态API");

                // 为POST、PUT、PATCH方法设置请求体
                String method = apiDefinition.getMethod().toUpperCase();
                if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
                    operation.setRequestBody(buildRequestBody(publishedVersion));
                }

                // 设置对应的HTTP方法
                switch (method) {
                    case "GET":
                        pathItem.setGet(operation);
                        break;
                    case "POST":
                        pathItem.setPost(operation);
                        break;
                    case "PUT":
                        pathItem.setPut(operation);
                        break;
                    case "DELETE":
                        pathItem.setDelete(operation);
                        break;
                    case "PATCH":
                        pathItem.setPatch(operation);
                        break;
                }
                paths.addPathItem(apiDefinition.getPath(), pathItem);
            }
        }

        openAPI.setPaths(paths);
        return openAPI;
    }

    /**
     * 从路径中提取路径参数
     *
     * @param path API路径
     * @return 路径参数列表
     */
    private List<Parameter> extractPathParameters(String path) {
        List<Parameter> pathParameters = new ArrayList<>();
        
        // 使用正则表达式匹配 {param} 格式的路径参数
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(path);
        
        while (matcher.find()) {
            String paramName = matcher.group(1);
            PathParameter pathParam = new PathParameter();
            pathParam.setName(paramName);
            pathParam.setRequired(true);
            pathParam.setSchema(new StringSchema());
            pathParam.setDescription("路径参数: " + paramName);
            pathParameters.add(pathParam);
        }
        
        return pathParameters;
    }

    /**
     * 构建查询参数列表
     *
     * @param apiVersion API版本
     * @return 查询参数列表
     */
    private List<Parameter> buildQueryParameters(ApiVersion apiVersion) {
        List<io.swagger.v3.oas.models.parameters.Parameter> parameters = new ArrayList<>();

        // 添加分页参数（如果支持分页）
        if (apiVersion.getSupportsPaging() != null && apiVersion.getSupportsPaging() == 1) {
            QueryParameter pageNumParam = new QueryParameter();
            pageNumParam.setName("_pageNum");
            pageNumParam.setSchema(new IntegerSchema());
            pageNumParam.setDescription("页码");
            parameters.add(pageNumParam);

            QueryParameter pageSizeParam = new QueryParameter();
            pageSizeParam.setName("_pageSize");
            pageSizeParam.setSchema(new IntegerSchema());
            pageSizeParam.setDescription("每页记录数");
            parameters.add(pageSizeParam);
        }

        // 根据paramDefinition构建更多参数
        if (apiVersion.getParamDefinition() != null && !apiVersion.getParamDefinition().isEmpty()) {
            try {
                // 解析参数定义
                List<Map<String, Object>> paramDefinitions = new ObjectMapper()
                        .readValue(apiVersion.getParamDefinition(), List.class);

                // 为每个参数定义创建QueryParameter
                for (Map<String, Object> paramDef : paramDefinitions) {
                    String paramName = (String) paramDef.get("name");
                    // 跳过分页参数，因为它们已经被特别处理过了
                    if ("_pageNum".equals(paramName) || "_pageSize".equals(paramName)) {
                        continue;
                    }
                    QueryParameter param = new QueryParameter();
                    param.setName(paramName);
                    param.setDescription((String) paramDef.get("description"));
                    param.setRequired(false); // 查询参数通常不是必需的

                    // 根据类型设置schema
                    String type = (String) paramDef.get("type");
                    if ("number".equals(type) || "integer".equals(type)) {
                        param.setSchema(new IntegerSchema());
                    } else if ("boolean".equals(type)) {
                        param.setSchema(new BooleanSchema());
                    } else if ("array".equals(type)) {
                        ArraySchema arraySchema = new ArraySchema();
                        arraySchema.setItems(new StringSchema());
                        param.setSchema(arraySchema);
                    } else {
                        param.setSchema(new StringSchema());
                    }

                    parameters.add(param);
                }
            } catch (Exception e) {
                // 如果解析失败，从SQL模板中提取参数
                extractParametersFromSqlTemplate(apiVersion.getSqlTemplate(), parameters);
            }
        } else {
            // 如果没有参数定义，从SQL模板中提取参数
            extractParametersFromSqlTemplate(apiVersion.getSqlTemplate(), parameters);
        }

        return parameters;
    }

    /**
     * 从SQL模板中提取参数名称
     *
     * @param sqlTemplate SQL模板
     * @param parameters  参数列表
     */
    private void extractParametersFromSqlTemplate(String sqlTemplate, List<Parameter> parameters) {
        if (sqlTemplate == null || sqlTemplate.isEmpty()) {
            return;
        }

        // 使用正则表达式匹配 #{paramName} 格式的参数
        Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(sqlTemplate);
        Set<String> paramNames = new HashSet<>();

        while (matcher.find()) {
            paramNames.add(matcher.group(1));
        }

        // 为每个参数创建QueryParameter
        for (String paramName : paramNames) {
            // 跳过分页参数，因为它们已经被特别处理过了
            if ("_pageNum".equals(paramName) || "_pageSize".equals(paramName)) {
                continue;
            }

            QueryParameter param = new QueryParameter();
            param.setName(paramName);
            param.setSchema(new StringSchema());
            param.setDescription("参数: " + paramName);
            parameters.add(param);
        }
    }

    /**
     * 构建请求体
     *
     * @param apiVersion API版本
     * @return 请求体
     */
    private io.swagger.v3.oas.models.parameters.RequestBody buildRequestBody(ApiVersion apiVersion) {
        io.swagger.v3.oas.models.parameters.RequestBody requestBody = new io.swagger.v3.oas.models.parameters.RequestBody();

        Content content = new Content();
        MediaType mediaType = new MediaType();

        // 根据paramDefinition构建请求体内容
        if (apiVersion.getParamDefinition() != null && !apiVersion.getParamDefinition().isEmpty()) {
            try {
                Schema<?> schema = buildRequestBodySchemaFromParamDefinition(apiVersion.getParamDefinition());
                mediaType.setSchema(schema);
            } catch (Exception e) {
                mediaType.setSchema(new ObjectSchema());
            }
        } else {
            // 默认对象类型
            mediaType.setSchema(new ObjectSchema());
        }

        content.addMediaType("application/json", mediaType);
        requestBody.setContent(content);

        return requestBody;
    }

    /**
     * 从参数定义构建请求体Schema
     *
     * @param paramDefinition 参数定义JSON字符串
     * @return 请求体Schema
     * @throws Exception 解析异常
     */
    private Schema<?> buildRequestBodySchemaFromParamDefinition(String paramDefinition) throws Exception {
        ObjectSchema schema = new ObjectSchema();
        schema.setDescription("请求参数");

        // 解析参数定义
        List<Map<String, Object>> paramDefinitions = new ObjectMapper()
                .readValue(paramDefinition, List.class);

        // 为每个参数定义创建Schema属性
        for (Map<String, Object> paramDef : paramDefinitions) {
            String name = (String) paramDef.get("name");
            String type = (String) paramDef.get("type");
            String description = (String) paramDef.get("description");
            Boolean required = (Boolean) paramDef.get("required");

            Schema<?> propertySchema;
            // 根据类型设置schema
            switch (type) {
                case "integer":
                case "number":
                    propertySchema = new IntegerSchema();
                    break;
                case "boolean":
                    propertySchema = new BooleanSchema();
                    break;
                case "array":
                    ArraySchema arraySchema = new ArraySchema();
                    arraySchema.setItems(new StringSchema());
                    propertySchema = arraySchema;
                    break;
                case "object":
                    propertySchema = new ObjectSchema();
                    break;
                default:
                    propertySchema = new StringSchema();
                    break;
            }

            propertySchema.setDescription(description);
            
            // 设置示例值
            Object example = paramDef.get("example");
            if (example != null) {
                if (propertySchema instanceof IntegerSchema && example instanceof Number) {
                    ((IntegerSchema) propertySchema).setExample(((Number) example).intValue());
                } else if (propertySchema instanceof StringSchema) {
                    ((StringSchema) propertySchema).setExample(example.toString());
                } else if (propertySchema instanceof BooleanSchema && example instanceof Boolean) {
                    ((BooleanSchema) propertySchema).setExample((Boolean) example);
                }
            }
            
            schema.addProperty(name, propertySchema);
            
            // 添加必需字段
            if (required != null && required) {
                schema.addRequiredItem(name);
            }
        }

        return schema;
    }

    /**
     * 构建响应
     *
     * @param apiVersion API版本
     * @return 响应定义
     */
    private ApiResponses buildResponses(ApiVersion apiVersion) {
        ApiResponses responses = new ApiResponses();

        // 成功响应 (200)
        ApiResponse successResponse = new ApiResponse();
        successResponse.setDescription("成功响应");

        Content successContent = new Content();
        MediaType successMediaType = new MediaType();
        
        // 构建成功响应Schema
        Schema<?> successSchema = buildSuccessResponseSchema(apiVersion);
        successMediaType.setSchema(successSchema);
        
        successContent.addMediaType("application/json", successMediaType);
        successResponse.setContent(successContent);
        responses.addApiResponse("200", successResponse);

        // 错误响应 (400)
        ApiResponse badRequestResponse = new ApiResponse();
        badRequestResponse.setDescription("请求参数错误");
        
        Content errorContent = new Content();
        MediaType errorMediaType = new MediaType();
        
        Schema<?> errorSchema = new ObjectSchema()
                .addProperty("code", new IntegerSchema().example(400))
                .addProperty("message", new StringSchema().example("请求参数错误"))
                .addProperty("timestamp", new StringSchema().example("2024-01-01T00:00:00"));
        errorMediaType.setSchema(errorSchema);
        
        errorContent.addMediaType("application/json", errorMediaType);
        badRequestResponse.setContent(errorContent);
        responses.addApiResponse("400", badRequestResponse);

        // 未授权响应 (401)
        ApiResponse unauthorizedResponse = new ApiResponse();
        unauthorizedResponse.setDescription("未授权访问");
        
        Content unauthorizedContent = new Content();
        MediaType unauthorizedMediaType = new MediaType();
        
        Schema<?> unauthorizedSchema = new ObjectSchema()
                .addProperty("code", new IntegerSchema().example(401))
                .addProperty("message", new StringSchema().example("未授权访问"))
                .addProperty("timestamp", new StringSchema().example("2024-01-01T00:00:00"));
        unauthorizedMediaType.setSchema(unauthorizedSchema);
        
        unauthorizedContent.addMediaType("application/json", unauthorizedMediaType);
        unauthorizedResponse.setContent(unauthorizedContent);
        responses.addApiResponse("401", unauthorizedResponse);

        // 禁止访问响应 (403)
        ApiResponse forbiddenResponse = new ApiResponse();
        forbiddenResponse.setDescription("禁止访问");
        
        Content forbiddenContent = new Content();
        MediaType forbiddenMediaType = new MediaType();
        
        Schema<?> forbiddenSchema = new ObjectSchema()
                .addProperty("code", new IntegerSchema().example(403))
                .addProperty("message", new StringSchema().example("禁止访问"))
                .addProperty("timestamp", new StringSchema().example("2024-01-01T00:00:00"));
        forbiddenMediaType.setSchema(forbiddenSchema);
        
        forbiddenContent.addMediaType("application/json", forbiddenMediaType);
        forbiddenResponse.setContent(forbiddenContent);
        responses.addApiResponse("403", forbiddenResponse);

        // 服务器错误响应 (500)
        ApiResponse serverErrorResponse = new ApiResponse();
        serverErrorResponse.setDescription("服务器内部错误");
        
        Content serverErrorContent = new Content();
        MediaType serverErrorMediaType = new MediaType();
        
        Schema<?> serverErrorSchema = new ObjectSchema()
                .addProperty("code", new IntegerSchema().example(500))
                .addProperty("message", new StringSchema().example("服务器内部错误"))
                .addProperty("timestamp", new StringSchema().example("2024-01-01T00:00:00"));
        serverErrorMediaType.setSchema(serverErrorSchema);
        
        serverErrorContent.addMediaType("application/json", serverErrorMediaType);
        serverErrorResponse.setContent(serverErrorContent);
        responses.addApiResponse("500", serverErrorResponse);

        return responses;
    }

    /**
     * 构建成功响应Schema
     *
     * @param apiVersion API版本
     * @return 成功响应Schema
     */
    private Schema<?> buildSuccessResponseSchema(ApiVersion apiVersion) {
        // 创建标准的响应结构
        ObjectSchema responseSchema = new ObjectSchema();
        responseSchema.addProperty("code", new IntegerSchema().example(200));
        responseSchema.addProperty("message", new StringSchema().example("成功"));
        responseSchema.addProperty("timestamp", new StringSchema().example("2024-01-01T00:00:00"));
        
        // 尝试根据responseTemplate构建data部分的Schema
        Schema<?> dataSchema = buildDataSchemaFromResponseTemplate(apiVersion);
        responseSchema.addProperty("data", dataSchema);
        
        return responseSchema;
    }

    /**
     * 根据响应模板构建data部分的Schema
     *
     * @param apiVersion API版本
     * @return data部分的Schema
     */
    private Schema<?> buildDataSchemaFromResponseTemplate(ApiVersion apiVersion) {
        // 默认返回对象类型Schema
        ObjectSchema schema = new ObjectSchema();
        
        // 如果有响应模板，尝试解析
        if (apiVersion.getResponseTemplate() != null && !apiVersion.getResponseTemplate().isEmpty()) {
            try {
                // 尝试解析JSON响应模板
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> templateMap = mapper.readValue(apiVersion.getResponseTemplate(), Map.class);
                
                // 根据模板构建Schema
                for (Map.Entry<String, Object> entry : templateMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    
                    if (value instanceof Integer) {
                        schema.addProperty(key, new IntegerSchema().example((Integer) value));
                    } else if (value instanceof Boolean) {
                        schema.addProperty(key, new BooleanSchema().example((Boolean) value));
                    } else if (value instanceof Number) {
                        schema.addProperty(key, new NumberSchema().example((Number) value));
                    } else if (value instanceof List) {
                        ArraySchema arraySchema = new ArraySchema();
                        // 尝试推断数组元素类型
                        if (!((List<?>) value).isEmpty()) {
                            Object firstElement = ((List<?>) value).get(0);
                            if (firstElement instanceof Integer) {
                                arraySchema.setItems(new IntegerSchema());
                            } else if (firstElement instanceof String) {
                                arraySchema.setItems(new StringSchema());
                            } else if (firstElement instanceof Boolean) {
                                arraySchema.setItems(new BooleanSchema());
                            } else {
                                arraySchema.setItems(new ObjectSchema());
                            }
                        } else {
                            arraySchema.setItems(new ObjectSchema());
                        }
                        schema.addProperty(key, arraySchema);
                    } else {
                        schema.addProperty(key, new StringSchema().example(value != null ? value.toString() : null));
                    }
                }
            } catch (Exception e) {
                // 如果解析失败，返回通用对象Schema
                schema.setDescription("动态API响应数据");
            }
        } else {
            // 没有响应模板，返回通用对象Schema
            schema.setDescription("动态API响应数据");
        }
        
        return schema;
    }
}