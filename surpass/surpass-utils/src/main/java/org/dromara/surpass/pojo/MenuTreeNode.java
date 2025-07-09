package org.dromara.surpass.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tomsun28
 * @date 14:52 2018/3/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeNode extends BaseTreeNode {

    private String code;

    private String name;

    private String uri;

    private Short type;

    private String method;

    private String icon;

    private Short status;

    private Date createTime;

    private Date updateTime;
}
