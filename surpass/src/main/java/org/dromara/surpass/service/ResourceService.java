package org.dromara.surpass.service;

import org.dromara.surpass.pojo.entity.AuthResource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author tomsun28
 * @date 00:13 2019-08-01
 */
public interface ResourceService {

    /**
     * add uri resource
     * @param authResource resource
     * @return success-true failed-false
     */
    boolean addResource(AuthResource authResource);

    /**
     * Determine whether the resource already exists
     * @param authResource resource
     * @return existed-true no-false
     */
    boolean isResourceExist(AuthResource authResource);

    /**
     * update uri resource
     * @param authResource resource
     * @return success-true failed-false
     */
    boolean updateResource(AuthResource authResource);

    /**
     * delete uri resource
     * @param resourceId resource ID
     * @return success-true no existed-false
     */
    boolean deleteResource(Long resourceId);

    /**
     * get all resources
     * @return resource list
     */
    Optional<List<AuthResource>> getAllResource();

    /**
     * get resource by page
     * @param currentPage current page
     * @param pageSize page size
     * @return Page of resource
     */
    Page<AuthResource> getPageResource(Integer currentPage, Integer pageSize);

    /**
     * get enabled resource-path-role eg: /api/v2/host===post===[role2,role3,role4]
     * @return resource-path-role
     */
    Set<String> getAllEnableResourcePath();

    /**
     * get disable resource-path-role eg: /api/v2/host===post===[role2,role3,role4]
     * @return resource-path-role
     */
    Set<String> getAllDisableResourcePath();
}
