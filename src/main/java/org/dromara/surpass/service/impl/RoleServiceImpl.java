package org.dromara.surpass.service.impl;

import com.usthe.sureness.matcher.TreePathRoleMatcher;

import org.dromara.surpass.dao.AuthResourceDao;
import org.dromara.surpass.dao.AuthRoleDao;
import org.dromara.surpass.dao.AuthRoleResourceBindDao;
import org.dromara.surpass.pojo.entity.AuthResource;
import org.dromara.surpass.pojo.entity.AuthRole;
import org.dromara.surpass.pojo.entity.AuthRoleResourceBind;
import org.dromara.surpass.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author tomsun28
 * @date 13:10 2019-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private AuthRoleDao authRoleDao;

    @Autowired
    private AuthResourceDao authResourceDao;

    @Autowired
    private AuthRoleResourceBindDao roleResourceBindDao;

    @Autowired
    private TreePathRoleMatcher treePathRoleMatcher;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean isRoleExist(AuthRole authRole) {
        AuthRole role = AuthRole.builder()
                .name(authRole.getName()).code(authRole.getCode()).build();
        return authRoleDao.exists(Example.of(role));
    }

    @Override
    @Cacheable(value = "role", key = "#authRole.id", unless = "#result eq null")
    public boolean addRole(AuthRole authRole) {
        if (isRoleExist(authRole)) {
            return false;
        } else {
            authRoleDao.saveAndFlush(authRole);
            return true;
        }
    }

    @Override
    public boolean updateRole(AuthRole authRole) {
        if (authRoleDao.existsById(authRole.getId())) {
            authRoleDao.saveAndFlush(authRole);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteRole(Long roleId) {
        if (authRoleDao.existsById(roleId)) {
            authRoleDao.deleteById(roleId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<List<AuthRole>> getAllRole() {
        List<AuthRole> roleList = authRoleDao.findAll();
        return Optional.of(roleList);
    }

    @Override
    public Page<AuthRole> getPageRole(Integer currentPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        return authRoleDao.findAll(pageRequest);
    }

    @Override
    public Page<AuthResource> getPageResourceOwnRole(Long roleId, Integer currentPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.Direction.ASC, "id");
        return authResourceDao.findRoleOwnResource(roleId, pageRequest);
    }

    @Override
    public Page<AuthResource> getPageResourceNotOwnRole(Long roleId, Integer currentPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.Direction.ASC, "id");
        return authResourceDao.findRoleNotOwnResource(roleId, pageRequest);
    }

    @Override
    public void authorityRoleResource(Long roleId, Long resourceId) {
        // Determine whether this resource and role exist
        if (!authRoleDao.existsById(roleId) || !authResourceDao.existsById(resourceId)) {
            throw new DataConflictException("roleId or resourceId not exist");
        }
        // insert it in database, if existed the unique index will work
        AuthRoleResourceBind bind = AuthRoleResourceBind
                .builder().roleId(roleId).resourceId(resourceId).build();
        roleResourceBindDao.saveAndFlush(bind);
        // refresh resource path data tree
        treePathRoleMatcher.rebuildTree();
        redisTemplate.delete("tom-enable-resource");
        redisTemplate.delete("tom-disable-resource");
    }

    @Override
    public void deleteAuthorityRoleResource(Long roleId, Long resourceId) {
        roleResourceBindDao.deleteRoleResourceBind(roleId, resourceId);
        // refresh resource path data tree
        treePathRoleMatcher.rebuildTree();
        redisTemplate.delete("tom-enable-resource");
        redisTemplate.delete("tom-disable-resource");
    }
}
