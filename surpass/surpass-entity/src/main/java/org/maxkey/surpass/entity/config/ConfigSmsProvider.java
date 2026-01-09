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






package org.maxkey.surpass.entity.config;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_config_sms_provider")
@Entity
public class ConfigSmsProvider extends JpaEntity implements Serializable {
	@Serial
    private static final long serialVersionUID = -4595539647817265938L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    String provider;

    @Column
    String message;

    @Column
    String appKey;

    @Column
    String appSecret;

    @Column
    String templateId;

    @Column
    String signName;

    @Column
    String smsSdkAppId;

    @Column
    String description;

    @Column
    int status;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;

    @Column
    private String modifiedBy;

    @Column
    private Date modifiedDate;
}
