package org.dromara.surpass.entity.history.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.entity.JpaPage;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/30 9:57
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class HistoryOpenapiPageDto extends JpaPage {
    /** Request ID（链路追踪） */
    private String requestId;

    /** Client ID */
    private String clientId;

    /** 应用名称 */
    private String appName;

    /** 应用 ID */
    private String appId;

    /** 资源 ID */
    private String resourceId;

    /** 资源名称 */
    private String resourceName;

    /** 请求 URI */
    private String requestUri;

    /** 请求方法 GET / POST / PUT / DELETE */
    private String requestMethod;

    /** 访问 IP */
    private String ipAddr;

    /** 国家 */
    private String country;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 是否已认证（true / false） */
    private String authned;

    /** 访问结果（ALLOW / DENY / FAIL） */
    private String access;

    /** 访问开始时间 */
    private Date startTime;

    /** 访问结束时间 */
    private Date endTime;

    /** 最小耗时（ms） */
    private Long minAccessCost;

    /** 最大耗时（ms） */
    private Long maxAccessCost;
}
