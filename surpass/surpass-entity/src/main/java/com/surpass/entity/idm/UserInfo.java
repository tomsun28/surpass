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






package com.surpass.entity.idm;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * .
 * @author Crystal.Sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("surpass_userinfo")
public class UserInfo extends BaseEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 6402443942083382236L;

    public static final String CLASS_TYPE = "UserInfo";

    public  static final String DEFAULT_PASSWORD_SUFFIX = "MaxKey@888";

    @TableField(exist = false)
    String sessionId;

    @TableId(type = IdType.ASSIGN_ID)
    String id;


    @NotEmpty(message = "登录账号不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "登录账号的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    protected String username;

    @NotEmpty(message = "密码不能为空", groups = {AddGroup.class})
    protected String password;

    protected String decipherable;

    protected String sharedSecret;

    protected String sharedCounter;

    /**
     * "Employee", "Supplier","Dealer","Contractor",Partner,Customer "Intern",
     * "Temp", "External", and "Unknown" .
     */
    @NotEmpty(message = "用户类型不能为空", groups = {AddGroup.class, EditGroup.class})
    protected String userType;

    @NotEmpty(message = "用户状态不能为空", groups = {AddGroup.class, EditGroup.class})
    protected String userState;


    // for user name
    @NotEmpty(message = "姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "姓名的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    protected String displayName;

    @Size(max = 32, message = "昵称的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    protected String nickName;

    @NotNull(message = "排序序号不能为空", groups = {AddGroup.class, EditGroup.class})
    protected Integer sortIndex;

    @Schema(name = "nameZhSpell", description = "名字中文拼音")
    protected String nameZhSpell;

    @Schema(name = "nameZhShortSpell", description = "名字中文拼音简称")
    protected String nameZhShortSpell;

    protected String  pictureId;

    protected String email;

    protected int emailVerified;

    protected String mobile;

    protected int mobileVerified;

    protected String passwordQuestion;

    protected String passwordAnswer;

    protected Date passwordLastSetTime;

    protected int badPasswordCount;

    protected Date badPasswordTime;

    protected Date unLockTime;

    protected int isLocked;

    protected Date lastLoginTime;

    protected String lastLoginIp;

    protected Date lastLogoffTime;

    protected int passwordSetType;

    protected Integer loginCount;

    @TableField(exist = false)
    protected String regionHistory;

    @TableField(exist = false)
    protected String passwordHistory;

    protected Integer loginFailedCount;

    protected Date loginFailedTime;

    protected String locale;

    protected String timeZone;

    protected String preferredLanguage;

    protected int isOnline;

    protected String ldapDn;

    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    int status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
    String deleted;


    String description;

	String bookId;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    String instId;

    @TableField(exist = false)
    String syncId;

    @TableField(exist = false)
    String syncName;

    @TableField(exist = false)
    String originId;

    @TableField(exist = false)
    String originId2;

    @TableField(exist = false)
    String gradingUserId;

    public UserInfo(String username) {
    	this.username = username;
    }

    public void clearSensitive() {
		this.setPassword("");
		this.setDecipherable("");
	}
}
