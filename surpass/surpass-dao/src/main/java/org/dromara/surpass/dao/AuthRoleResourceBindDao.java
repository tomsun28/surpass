package org.dromara.surpass.dao;


import org.dromara.surpass.pojo.entity.AuthResource;
import org.dromara.surpass.pojo.entity.AuthRoleResourceBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author tomsun28
 * @date 16:43 2019-07-27
 */
public interface AuthRoleResourceBindDao extends JpaRepository<AuthRoleResourceBind, Long> {

    /**
     * Query the resources owned by the current role
     * @param roleId roleId
     * @return resource list
     */
    @Query("select rs from AuthResource rs, AuthRoleResourceBind bind " +
            "where rs.id = bind.resourceId and bind.roleId = :roleId")
    List<AuthResource> findRoleBindResourceList(@Param("roleId") Long roleId);

    /**
     * delete record which roleId and resource equals this
     * @param roleId roleID
     * @param resourceId resourceId
     */
    @Query("delete from AuthRoleResourceBind bind " +
            "where bind.roleId = :roleId and bind.resourceId = :resourceId")
    void deleteRoleResourceBind(@Param("roleId") Long roleId,@Param("resourceId") Long resourceId);
}
