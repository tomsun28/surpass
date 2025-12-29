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






package com.surpass.entity.history;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;

/**
 * .
 * @author Crystal.Sea
 *
 */

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "surpass_history_openapi")
@Entity
public class HistoryOpenapi extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6560201093784960493L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    String requestId;

    @Column
    String requestUri;

    @Column
    String requestMethod;

    @Column
    String authned;

    @Column
    String access;

    @Column
    String clientId;

    @Column
    String appId;

    @Column
    String appName;

    @Column
    String resourceId;

    @Column
    String resourceName;
    
    @Column
    String resourceUri;

    @Column
    Date accessTime;
    
    @Column
    Long accessCost;

}
