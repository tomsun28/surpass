package org.dromara.surpass.pojo.entity.permissions;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.surpass.constants.ConstsRoles;
import org.dromara.surpass.validate.AddGroup;
import org.dromara.surpass.validate.EditGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/16 16:14
 */

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "surpass_roles")
@Entity
public class Roles extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3645849977354053420L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @NotEmpty(message = "用户组编码不能为空", groups = {AddGroup.class, EditGroup.class})
    @Column
    String roleCode;

    @NotEmpty(message = "用户组名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Column
    String roleName;

    @Column
    String pattern;

    @Column
    String category;

    @Column
    String filters ;

    @Column
    String orgIdsList;

    @Column
    int isdefault;

    @Column
    String description;

    @Column
    int status;

    @Column
    @SoftDelete
    String deleted;

    private String instName;

    String gradingUserId;


    public Roles(String id) {
        this.id = id;
    }

    public Roles(String id, String roleName, int isdefault) {
        super();
        this.id = id;
        this.roleName = roleName;
        this.isdefault = isdefault;
    }

    public Roles(String id, String roleCode,String roleName, int isdefault) {
        super();
        this.id = id;
        this.roleCode = roleName;
        this.roleName = roleName;
        this.isdefault = isdefault;
    }

    /**
     * ROLE_ALL_USER must be
     * 		1, dynamic
     * 		2, all orgIdsList
     *		3, not filters
     */
    public void setDefaultAllUser() {
        this.pattern = ConstsRoles.Pattern.DYNAMIC;
        this.orgIdsList ="";
        this.filters ="";
    }
}
