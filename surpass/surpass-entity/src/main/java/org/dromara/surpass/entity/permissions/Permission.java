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






package org.dromara.surpass.entity.permissions;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.surpass.constants.ConstsStatus;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_permission")
public class Permission  extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -8783585691243853899L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    String roleId;
    
    @Column
    String appId;

    @Column
    String resourceId;
    
    @Column
    int category = 1;

    @Column
    int status = ConstsStatus.ACTIVE;

    @Column
	private String instName;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;

    @Column
    private String modifiedBy;

    @Column
    private Date modifiedDate;

    public Permission(String roleId) {
        this.roleId = roleId;
    }

    /**
     * .
     * @param roleId String
     * @param resourceId String
     * @param resourceId String
     */
    public Permission(String id,String roleId, String resourceId ,String createdBy) {
        this.id = id;
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.createdBy = createdBy;
    }

    public String  getUniqueId() {
        return   roleId + "_" + resourceId;
    }

}
