package org.dromara.surpass.service;

import org.dromara.surpass.pojo.entity.AuthResource;
import org.dromara.surpass.pojo.entity.AuthRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author tomsun28
 * @date 00:14 2019-08-01
 */
public interface RoleService {

    /**
     * Determine whether the role already exists
     * @param authRole role
     * @return existed-true no-false
     */
    boolean isRoleExist(AuthRole authRole);

    /**
     * add role
     * @param authRole role
     * @return add success-true failed-false
     */
    boolean addRole(AuthRole authRole);

    /**
     * update role
     * @param authRole role
     * @return success-true failed-false
     */
    boolean updateRole(AuthRole authRole);

    /**
     * delete role
     * @param roleId role ID
     * @return success-true failed-false
     */
    boolean deleteRole(Long roleId);

    /**
     * get all role list
     * @return role list
     */
    Optional<List<AuthRole>> getAllRole();

    /**
     * get roles page
     * @param currentPage current page
     * @param pageSize page size
     * @return Page of roles
     */
    Page<AuthRole> getPageRole(Integer currentPage, Integer pageSize);

    /**
     * get pageable resources which this role owned
     * @param roleId role ID
     * @param currentPage current page
     * @param pageSize page size
     * @return Page of resources
     */
    Page<AuthResource> getPageResourceOwnRole(Long roleId, Integer currentPage, Integer pageSize);

    /**
     * get pageable resources which this role not owned
     * @param roleId role ID
     * @param currentPage current page
     * @param pageSize page size
     * @return Page of resources
     */
    Page<AuthResource> getPageResourceNotOwnRole(Long roleId, Integer currentPage, Integer pageSize);

    /**
     * authority this resource to this role
     * @param roleId role ID
     * @param resourceId resource ID
     */
    void authorityRoleResource(Long roleId, Long resourceId);

    /**
     * unAuthority this resource in this role
     * @param roleId role ID
     * @param resourceId resource ID
     */
    void deleteAuthorityRoleResource(Long roleId, Long resourceId);

}
