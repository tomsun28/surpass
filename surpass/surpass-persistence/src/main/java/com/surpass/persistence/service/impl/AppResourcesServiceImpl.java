package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesChangeDto;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.idm.Organizations;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.AppResourcesMapper;
import com.surpass.persistence.service.AppResourcesService;
import com.surpass.persistence.util.ResourceClassify;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.tree.MapTree;
import org.dromara.hutool.core.tree.TreeNode;
import org.dromara.hutool.core.tree.TreeUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.surpass.persistence.util.ResourceClassify.MENU;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/18 11:15
 */

@Service
@RequiredArgsConstructor
public class AppResourcesServiceImpl extends JpaServiceImpl<AppResourcesMapper, AppResources> implements AppResourcesService {

    private final AppResourcesMapper appResourcesMapper;

    @Override
    public Message<String> create(AppResourcesChangeDto dto) {
        AppResources appResources = BeanUtil.copyProperties(dto, AppResources.class);

        ResourceClassify classify = ResourceClassify.from(dto.getClassify());

        switch (classify) {
            case OPEN_API -> validateOpenApi(appResources);
            case BUTTON   -> validateButton(appResources);
            case MENU     -> validateMenu(appResources);
            case API      -> validateApi(appResources);
        }

        boolean result = super.insert(appResources);
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<String> updateResources(AppResourcesChangeDto dto) {
        AppResources appResources = BeanUtil.copyProperties(dto, AppResources.class);

        ResourceClassify classify = ResourceClassify.from(dto.getClassify());

        switch (classify) {
            case OPEN_API -> validateOpenApi(appResources);
            case BUTTON   -> validateButton(appResources);
            case MENU     -> validateMenu(appResources);
            case API      -> validateApi(appResources);
        }

        boolean result = super.update(appResources);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public Message<JpaPageResults<AppResources>> page(AppResourcesPageDto dto) {
        dto.build();
        JpaPageResults<AppResources> jpaPageResults = (JpaPageResults<AppResources>) this.buildPageResults(dto, getMapper().pageList(dto));
        return Message.ok(jpaPageResults);
    }

    @Override
    public Map<String, List<MapTree<String>>> tree(AppResourcesPageDto dto) {
        // 1. 一次性查询该应用下的所有资源
        List<AppResources> allResources = super.query(
                new LambdaQuery<AppResources>()
                        .eq(AppResources::getAppId, dto.getAppId())
        );

        // 2. 构建完整资源树
        List<MapTree<String>> resourceTree = buildTree(allResources);

        // 3. 构建菜单资源树（内存中过滤）
        List<MapTree<String>> menuTree = buildTree(
                allResources.stream()
                        .filter(r -> MENU.getCode().equals(r.getClassify()))
                        .toList()
        );

        // 4. 返回结果
        Map<String, List<MapTree<String>>> result = new HashMap<>(2);
        result.put("resources", resourceTree);
        result.put("resourcesMenu", menuTree);
        return result;
    }

    private List<MapTree<String>> buildTree(List<AppResources> resources) {
        if (ObjectUtils.isEmpty(resources)) {
            return List.of();
        }

        List<TreeNode<String>> nodes = resources.stream()
                .map(r -> new TreeNode<>(
                        r.getId(),
                        String.valueOf(r.getParentId()),
                        r.getName(),
                        r.getSortIndex()
                ))
                .toList();

        return TreeUtil.build(nodes, "null");
    }


    private void requireNotBlank(String value, int code, String message) {
        if (StringUtils.isBlank(value)) {
            throw new BusinessException(code, message);
        }
    }

    private void validateOpenApi(AppResources r) {
        requireNotBlank(r.getDatasourceId(), 50001, "请选择OpenApi所属的数据源");
        requireNotBlank(r.getPath(), 50001, "请填写请求地址");
        requireNotBlank(r.getMethod(), 50001, "请填写请求方式");
    }

    private void validateButton(AppResources r) {
        requireNotBlank(r.getActionType(), 50001, "请填写操作类型");
    }

    private void validateMenu(AppResources r) {
        requireNotBlank(r.getPath(), 50001, "请填写请求路径");
    }

    private void validateApi(AppResources r) {
        requireNotBlank(r.getPath(), 50001, "请填写请求地址");
        requireNotBlank(r.getMethod(), 50001, "请填写请求方式");
    }
}
