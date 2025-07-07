package org.dromara.surpass.service.impl;

import org.dromara.surpass.dao.AuthResourceDao;
import org.dromara.surpass.pojo.entity.AuthResource;
import org.dromara.surpass.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author tomsun28
 * @date 13:09 2019-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private AuthResourceDao authResourceDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean addResource(AuthResource authResource) {
        if (isResourceExist(authResource)) {
            return false;
        } else {
            authResourceDao.saveAndFlush(authResource);
            return true;
        }
    }

    @Override
    public boolean isResourceExist(AuthResource authResource) {
        AuthResource resource = AuthResource.builder()
                .uri(authResource.getUri())
                .method(authResource.getMethod())
                .build();
        Example<AuthResource> example = Example.of(resource);
        return authResourceDao.exists(example);
    }

    @Override
    public boolean updateResource(AuthResource authResource) {
        if (authResourceDao.existsById(authResource.getId())) {
            authResourceDao.saveAndFlush(authResource);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        if (authResourceDao.existsById(resourceId)) {
            authResourceDao.deleteById(resourceId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<List<AuthResource>> getAllResource() {
        List<AuthResource> resourceList = authResourceDao.findAll();
        return Optional.of(resourceList);
    }

    @Override
    public Page<AuthResource> getPageResource(Integer currentPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        return authResourceDao.findAll(pageRequest);
    }

    @Override
    public Set<String> getAllEnableResourcePath() {
        Set<String> enableResource = redisTemplate.opsForSet().members("tom-enable-resource");
        if (enableResource == null || enableResource.isEmpty()) {
            Optional<List<String>> optional = authResourceDao.getEnableResourcePathRoleData();
            enableResource =  optional.<Set<String>>map(HashSet::new).orElseGet(() -> new HashSet<>(0));
        }
        enableResource.forEach(resource -> redisTemplate.opsForSet().add("tom-enable-resource", resource));
        return enableResource;
    }

    @Override
    public Set<String> getAllDisableResourcePath() {
        Set<String> disableResource = redisTemplate.opsForSet().members("tom-disable-resource");
        if (disableResource == null || disableResource.isEmpty()) {
            Optional<List<String>> optional = authResourceDao.getDisableResourcePathData();
            disableResource =  optional.<Set<String>>map(HashSet::new).orElseGet(() -> new HashSet<>(0));
        }
        disableResource.forEach(resource -> redisTemplate.opsForSet().add("tom-disable-resource", resource));
        return disableResource;
    }
}
