package org.dromara.surpass.dao;


import org.dromara.surpass.pojo.entity.AuthRole;
import org.dromara.surpass.pojo.entity.AuthUserRoleBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author tomsun28
 * @date 16:44 2019-07-27
 */
public interface AuthUserRoleBindDao extends JpaRepository<AuthUserRoleBind, Long> {

    /**
     * Query the role owned by the current user
     * @param userId userId
     * @return role list
     */
    @Query("select ar from AuthRole ar, AuthUserRoleBind bind " +
            "where ar.id = bind.roleId and bind.userId = :userId")
    List<AuthRole> findUserBindRoleList(@Param("userId") Long userId);

    /**
     * delete record which roleId and userId equals this
     * @param roleId roleID
     * @param userId userId
     */
    @Query("delete from AuthUserRoleBind bind " +
            "where bind.roleId = :roleId and bind.userId = :userId")
    void deleteRoleResourceBind(@Param("roleId") Long roleId,@Param("userId") Long userId);
}
