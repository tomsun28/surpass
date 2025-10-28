/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
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


package com.surpass.entity.config;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置表 jbx_config
 *
 * @author Wuyan
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jbx_config")
public class ConfigSys extends BaseEntity {

    /**
	 *
	 */
	private static final long serialVersionUID = -7790580992219005409L;

	/**
     * 参数主键
     */
    @TableId(value = "config_id")
    private String configId;

    /**
     * 账簿ID
     */
    private String bookId;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过{max}个字符")
    private String configName;

    /**
     * 参数键名
     */
    @NotBlank(message = "参数键名长度不能为空")
    @Size(max = 100, message = "参数键名长度不能超过{max}个字符")
    private String configKey;

    /**
     * 参数键值
     */
    @NotBlank(message = "参数键值不能为空")
    @Size(max = 500, message = "参数键值长度不能超过{max}个字符")
    private String configValue;

    /**
     * 系统内置（y是 n否）
     */
    private String configType;

    /**
     * 备注
     */
    private String remark;

}
