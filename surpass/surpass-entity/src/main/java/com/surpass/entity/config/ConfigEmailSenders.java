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
@Table(name = "surpass_config_email_senders")
@Entity
public class ConfigEmailSenders extends JpaEntity implements Serializable {

	@Serial
    private static final long serialVersionUID = 5564960495591334956L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @Column
    private String account;

    @Column
    private String credentials;

    @Column
    private String smtpHost;

    @Column
    private Integer port;

    @Column
    private int sslSwitch;

    @Column
    private String sender;

    @Column
    private String encoding;

    @Column
    private String protocol;

    @Column
    private int status;

    @Column
    private String description;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;

    @Column
    private String modifiedBy;

    @Column
    private Date modifiedDate;
}
