package org.maxkey.surpass.persistence.service;

import org.dromara.hutool.core.tree.MapTree;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.app.AppResources;
import org.maxkey.surpass.entity.app.dto.AppResourcesChangeDto;
import org.maxkey.surpass.entity.app.dto.AppResourcesPageDto;

import java.util.List;
import java.util.Map;

public interface AppResourcesService extends IJpaService<AppResources> {
    Message<String> create(AppResourcesChangeDto appResourcesChangeDto);

    Message<String> updateResources(AppResourcesChangeDto appResourcesChangeDto);

    Message<JpaPageResults<AppResources>> page(AppResourcesPageDto appResourcesPageDto);

    Map<String, List<MapTree<String>>> tree(AppResourcesPageDto dto);

    AppResources findByPathAndMethod(String path, String method, String contextPath);

    Message<String> deleteResources(List<String> resourcesIds);
}
