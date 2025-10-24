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






package org.dromara.surpass.pojo.entity.idm;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.surpass.validate.AddGroup;
import org.dromara.surpass.validate.EditGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * .
 * @author Crystal.Sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "SURPASS_USERINFO")
@Entity
public class UserInfo extends JpaEntity implements Serializable {

    public static final String CLASS_TYPE = "UserInfo";

    public static final String DEFAULT_PASSWORD_SUFFIX = "MaxKey@888";
    @Serial
    private static final long serialVersionUID = 351149277321386098L;

    String sessionId;

    @Id
    @Column
    @GeneratedValue
    String id;


    @Column
    @NotEmpty(message = "登录账号不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "登录账号的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    protected String username;

    @Column
    @NotEmpty(message = "密码不能为空", groups = {AddGroup.class})
    protected String password;

    @Column
    protected String decipherable;

    @Column
    protected String sharedSecret;

    @Column
    protected String sharedCounter;

    /**
     * "Employee", "Supplier","Dealer","Contractor",Partner,Customer "Intern",
     * "Temp", "External", and "Unknown" .
     */
    @Column
    @NotEmpty(message = "用户类型不能为空", groups = {AddGroup.class, EditGroup.class})
    protected String userType;

    @Column
    @NotEmpty(message = "用户状态不能为空", groups = {AddGroup.class, EditGroup.class})
    protected String userState;


    // for user name
    @Column
    @NotEmpty(message = "姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "姓名的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    protected String displayName;

    @Column
    @Size(max = 32, message = "昵称的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    protected String nickName;

    @Column
    @NotNull(message = "排序序号不能为空", groups = {AddGroup.class, EditGroup.class})
    protected Integer sortIndex;

    @Column
    @Schema(name = "nameZhSpell", description = "名字中文拼音")
    protected String nameZhSpell;

    @Column
    @Schema(name = "nameZhShortSpell", description = "名字中文拼音简称")
    protected String nameZhShortSpell;

    @Column
    protected String  pictureId;

    @Column
    protected String email;

    @Column
    protected int emailVerified;

    @Column
    protected String mobile;

    @Column
    protected int mobileVerified;

    @Column
    protected String passwordQuestion;

    @Column
    protected String passwordAnswer;

    @Column
    protected Date passwordLastSetTime;

    @Column
    protected int badPasswordCount;

    @Column
    protected Date badPasswordTime;

    @Column
    protected Date unLockTime;

    @Column
    protected int isLocked;

    @Column
    protected Date lastLoginTime;

    @Column
    protected String lastLoginIp;

    @Column
    protected Date lastLogoffTime;

    @Column
    protected int passwordSetType;

    @Column
    protected Integer loginCount;

    protected String regionHistory;

    protected String passwordHistory;

    @Column
    protected Integer loginFailedCount;

    @Column
    protected Date loginFailedTime;

    @Column
    protected String locale;

    @Column
    protected String timeZone;

    @Column
    protected String preferredLanguage;

    @Column
    protected int isOnline;

    @Column
    protected String ldapDn;

    @Column
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    int status;

    @Column
    @SoftDelete
    String deleted;

    @Column
    String createdBy;

    @Column
    Date createdDate;

    @Column
    String modifiedBy;

    @Column
    Date modifiedDate;

    @Column
    String description;

    @Column(updatable = false)
    String instId;

    String syncId;

    String syncName;

    String originId;

    String originId2;

    String gradingUserId;

    public UserInfo(String username) {
    	this.username = username;
    }

    public void clearSensitive() {
		this.setPassword("");
		this.setDecipherable("");
	}
}
