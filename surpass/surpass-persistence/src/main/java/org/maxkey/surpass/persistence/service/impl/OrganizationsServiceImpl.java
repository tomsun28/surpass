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




package org.maxkey.surpass.persistence.service.impl;

import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.tree.MapTree;
import org.dromara.hutool.core.tree.TreeNode;
import org.dromara.hutool.core.tree.TreeUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.idm.Organizations;
import org.maxkey.surpass.entity.idm.dto.OrgPageDto;
import org.maxkey.surpass.enums.OrgsBusinessExceptionEnum;
import org.maxkey.surpass.exception.BusinessException;
import org.maxkey.surpass.persistence.mapper.OrganizationsMapper;
import org.maxkey.surpass.persistence.service.OrganizationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrganizationsServiceImpl extends JpaServiceImpl<OrganizationsMapper, Organizations> implements OrganizationsService {
    static final Logger logger = LoggerFactory.getLogger(OrganizationsServiceImpl.class);


    @Autowired
    OrganizationsMapper organizationsMapper;

    public OrganizationsMapper getMapper() {
        return organizationsMapper;
    }

    @Override
    @Transactional
    public boolean saveOneOrg(Organizations organization) {
        organization.setId(organization.generateId());

        //检查同级是否存在相同的组织机构名称
        saveCheckDuplicateOrgs(organization);
        //设置路径
        organization.setCodePath(generateIdPath(organization.getParentId(), organization.getId()));
        organization.setNamePath(generateNamePath(organization.getParentId(), organization.getOrgName()));

        return super.insert(organization);
    }

    /**
     * @Description: 设置组织ID路径
     */
    private String generateIdPath(String parentId, String currentId) {
        if (StringUtils.isNotBlank(parentId)) {
            String parentPath = generateIdPathRecursive(parentId);
            if (parentPath.isEmpty()) {
                return "/" + currentId;
            } else {
                return "/" + parentPath + "/" + currentId;
            }
        }
        return "/" + currentId;
    }

    /**
     * @Description: 设置组织ID路径
     */
    private String generateIdPathRecursive(String parentId) {

        if (StringUtils.isBlank(parentId)) {
            return "";
        }

        Organizations parent = get(parentId);
        if (Objects.isNull(parent)) {
            return "";
        }

        if (parent.getId().equals(parent.getParentId())) {
            return parent.getId();
        }

        String parentPath = generateIdPathRecursive(parent.getParentId());
        if (parentPath.isEmpty()) {
            return parentId;
        } else {
            return parentPath + "/" + parentId;
        }
    }

    /**
     * @Description: 设置组织名称路径
     */
    private String generateNamePath(String parentId, String currentName) {
        if (StringUtils.isNotBlank(parentId)) {
            return "/" + generateNamePathRecursive(parentId) + "/" + currentName;
        }
        return "/" + currentName;
    }

    /**
     * @Description: 设置组织名称路径
     */
    private String generateNamePathRecursive(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            Organizations parent = get(parentId);
            if (Objects.nonNull(parent) && !parent.getId().equals(parent.getParentId())) {
                String namePath = generateNamePathRecursive(parent.getParentId());
                if (namePath.isEmpty()) {
                    return parent.getOrgName();
                } else {
                    return namePath + "/" + parent.getOrgName();
                }
            } else if (Objects.nonNull(parent)) {
                return parent.getOrgName();
            }
        }
        return "";
    }


    @Override
    @Transactional
    public boolean updateOneOrg(Organizations organization) {
        //检查是否有重名的组织或者编码
        updateCheckDuplicateOrgs(organization);

        String parentId;
        if (Objects.nonNull(organization.getParentId())) {
            parentId = organization.getParentId();
        } else {
            parentId = "";
        }
        String currentId = organization.getId();
        if (Objects.equals(parentId, currentId)) {
            throw new BusinessException(
                    OrgsBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getCode(),
                    OrgsBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getMsg()
            );
        }

        //修改当前组织
        LambdaQuery<Organizations> queryWrapper = new LambdaQuery<>();
        queryWrapper.like(Organizations::getCodePath, organization.getId());
        queryWrapper.notEq(Organizations::getId, currentId);
        List<Organizations> orgInfos = super.query(queryWrapper);
        if (orgInfos.stream().anyMatch(orgInfo -> Objects.equals(parentId, orgInfo.getId()))) {
            throw new BusinessException(
                    OrgsBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getCode(),
                    OrgsBusinessExceptionEnum.ILLEGAL_MOVE_ORG.getMsg()
            );
        }
        //设置当前节点ID路径和名称路径
        organization.setCodePath(generateIdPath(parentId, currentId));
        organization.setNamePath(generateNamePath(parentId, organization.getOrgName()));

        boolean result = super.update(organization);

        List<Organizations> updatedOrgInfos = orgInfos.stream()
                .map(orgInfo -> {
                    String subParentId = orgInfo.getParentId();
                    String subId = orgInfo.getId();
                    String subOrgName = orgInfo.getOrgName();
                    orgInfo.setCodePath(generateIdPath(subParentId, subId));
                    orgInfo.setNamePath(generateNamePath(subParentId, subOrgName));
                    return orgInfo;
                }).toList();
        if (ObjectUtils.isNotEmpty(updatedOrgInfos)) {
            for (Organizations organizations : updatedOrgInfos) {
                super.update(organizations);
            }
        }


        return result;
    }

    public List<Organizations> queryOrgs(Organizations organization) {
        return getMapper().queryOrgs(organization);
    }

    public boolean delete(Organizations organization) {
        if (this.delete(organization.getId())) {
            return true;
        }
        return false;
    }

    /**
     * @Description: 检查同级是否存在相同的组织机构名称或者编码
     * @Param: [entity]
     * @return: void
     */
    public void saveCheckDuplicateOrgs(Organizations entity) {
        String orgName = entity.getOrgName();
        String parentId = entity.getParentId();
        String orgCode = entity.getOrgCode();

        //检查名称
        LambdaQuery<Organizations> wrapper = new LambdaQuery<>();
        wrapper.eq(Organizations::getParentId, parentId);
        wrapper.eq(Organizations::getOrgName, orgName);
        List<Organizations> organizationsList = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(organizationsList)) {
            throw new BusinessException(
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGS_EXIST.getCode(),
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGS_EXIST.getMsg()
            );
        }

        //检查编码
        LambdaQuery<Organizations> secondWrapper = new LambdaQuery<>();
        secondWrapper.eq(Organizations::getOrgCode, orgCode);
        List<Organizations> secondOrgs = super.query(secondWrapper);
        if (ObjectUtils.isNotEmpty(secondOrgs)) {
            throw new BusinessException(
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGSCODE_EXIST.getCode(),
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGSCODE_EXIST.getMsg()
            );
        }
    }

    /**
     * @Description: 检查是否有重名的组织或者编码
     * @Param: [entity]
     * @return: void
     */
    public void updateCheckDuplicateOrgs(Organizations entity) {
        String orgName = entity.getOrgName();
        String parentId = entity.getParentId();
        String orgCode = entity.getOrgCode();
        String id = entity.getId();

        //检查名称
        LambdaQuery<Organizations> wrapper = new LambdaQuery<>();
        wrapper.eq(Organizations::getParentId, parentId);
        wrapper.eq(Organizations::getOrgName, orgName);
        wrapper.notEq(Organizations::getId, id);
        List<Organizations> organizationsList = super.query(wrapper);
        if (ObjectUtils.isNotEmpty(organizationsList)) {
            throw new BusinessException(
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGS_EXIST.getCode(),
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGS_EXIST.getMsg()
            );
        }

        //检查编码
        LambdaQuery<Organizations> secondWrapper = new LambdaQuery<>();
        secondWrapper.eq(Organizations::getOrgCode, orgCode);
        secondWrapper.notEq(Organizations::getId, id);
        List<Organizations> secondOrgs = super.query(secondWrapper);
        if (ObjectUtils.isNotEmpty(secondOrgs)) {
            throw new BusinessException(
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGSCODE_EXIST.getCode(),
                    OrgsBusinessExceptionEnum.DUPLICATE_ORGSCODE_EXIST.getMsg()
            );
        }
    }

    @Override
    public List<MapTree<String>> tree(Organizations org) {
        LambdaQuery<Organizations> wrapper = new LambdaQuery<>();
        List<Organizations> orgInfos = super.query(wrapper);
        List<TreeNode<String>> treeNode = new ArrayList<>();
        orgInfos.forEach(temp -> treeNode.add(new TreeNode<>(temp.getId(),
                String.valueOf(temp.getParentId()),
                temp.getOrgName(), temp.getSortIndex())));

        List<MapTree<String>> tree = TreeUtil.build(treeNode, "null");
        if (ObjectUtils.isEmpty(tree)) {
            tree = new ArrayList<>();
        }
        return tree;
    }

    @Override
    public Message<JpaPageResults<Organizations>> pageList(OrgPageDto orgPageDto) {
        orgPageDto.build();
        JpaPageResults<Organizations> jpaPageResults = (JpaPageResults<Organizations>) this.buildPageResults(orgPageDto, getMapper().queryPageResults(orgPageDto));
        return new Message<>(Message.SUCCESS, jpaPageResults);
    }

}
