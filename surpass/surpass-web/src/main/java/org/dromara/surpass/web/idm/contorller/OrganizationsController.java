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




package org.dromara.surpass.web.idm.contorller;

import java.util.List;

import org.dromara.hutool.core.tree.MapTree;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.surpass.authn.annotation.CurrentUser;
import org.dromara.surpass.authn.web.AuthorizationUtils;
import org.dromara.surpass.constants.ConstsAct;
import org.dromara.surpass.constants.ConstsActResult;
import org.dromara.surpass.constants.ConstsEntryType;
import org.dromara.surpass.constants.ConstsRoles;
import org.dromara.surpass.entity.ExcelImport;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.idm.Organizations;
import org.dromara.surpass.entity.idm.UserInfo;
import org.dromara.surpass.entity.idm.dto.OrgPageDto;
import org.dromara.surpass.persistence.service.HistorySystemLogsService;
import org.dromara.surpass.persistence.service.OrganizationsExcelService;
import org.dromara.surpass.persistence.service.OrganizationsService;
import org.dromara.surpass.validate.AddGroup;
import org.dromara.surpass.validate.EditGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping({"/orgs"})
public class OrganizationsController {
    static final Logger logger = LoggerFactory.getLogger(OrganizationsController.class);

    @Autowired
    OrganizationsService organizationsService;

    @Autowired
    OrganizationsExcelService organizationsExcelService;

    @Autowired
    HistorySystemLogsService historySystemLogsService;

    @GetMapping(value = {"/fetch"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<JpaPageResults<Organizations>> fetch(@ParameterObject OrgPageDto dto, @CurrentUser UserInfo currentUser) {
        return organizationsService.pageList(dto);
    }

    @GetMapping(value = {"/query"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<List<Organizations>> query(@ModelAttribute Organizations org, @CurrentUser UserInfo currentUser) {
        logger.debug("-query  {}", org);
        LambdaQuery<Organizations> wrapper = new LambdaQuery<>();
        List<Organizations> orgList = organizationsService.query(wrapper);
        if (orgList != null) {
            return new Message<>(Message.SUCCESS, orgList);
        } else {
            return new Message<>(Message.FAIL);
        }
    }

    @GetMapping(value = {"/get/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<Organizations> get(@PathVariable("id") String id) {
        Organizations org = organizationsService.get(id);
        return new Message<>(org);
    }

    @PostMapping(value = {"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<Organizations> insert(@Validated(value = AddGroup.class)
                                         @RequestBody Organizations org, @CurrentUser UserInfo currentUser) {
        logger.debug("-Add  : {}", org);
        if (organizationsService.saveOneOrg(org)) {
            historySystemLogsService.log(
                    ConstsEntryType.ORGANIZATION,
                    org,
                    ConstsAct.CREATE,
                    ConstsActResult.SUCCESS,
                    currentUser);
            return new Message<>(Message.SUCCESS, org);
        } else {
            return new Message<>(Message.FAIL);
        }
    }

    @PutMapping(value = {"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<Organizations> update(@Validated(value = EditGroup.class)
                                         @RequestBody Organizations org,
                                         @CurrentUser UserInfo currentUser) {
        logger.debug("-update  : {}", org);
        if (organizationsService.updateOneOrg(org)) {
            historySystemLogsService.log(
                    ConstsEntryType.ORGANIZATION,
                    org,
                    ConstsAct.UPDATE,
                    ConstsActResult.SUCCESS,
                    currentUser);
            return new Message<>(Message.SUCCESS);
        } else {
            return new Message<>(Message.FAIL);
        }
    }

    @DeleteMapping(value = {"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<Organizations> delete(@RequestParam("ids") List<String> ids, @CurrentUser UserInfo currentUser) {
        logger.debug("-delete  ids : {} ", ids);
        if (organizationsService.deleteBatch(ids)) {
            historySystemLogsService.log(
                    ConstsEntryType.ORGANIZATION,
                    ids,
                    ConstsAct.DELETE,
                    ConstsActResult.SUCCESS,
                    currentUser);
            return new Message<>(Message.SUCCESS);
        } else {
            return new Message<>(Message.FAIL);
        }
    }

    @GetMapping(value = {"/tree"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<List<MapTree<String>>> tree(@ModelAttribute Organizations organization, @CurrentUser UserInfo currentUser) {
        logger.debug("-query  {}", organization);

        if (AuthorizationUtils.getAuthentication().getAuthorities().contains(ConstsRoles.ROLE_MANAGER)) {
            logger.debug("Has ROLE_MANAGERS {}", currentUser.getId());
            organization.setGradingUserId(currentUser.getId());
        }

        List<MapTree<String>> tree = organizationsService.tree(organization);
        return new Message<>(Message.SUCCESS, tree);

    }

    @RequestMapping(value = "/import")
    public Message<Organizations> importingOrganizations(
            @ModelAttribute("excelImportFile") ExcelImport excelImportFile,
            @CurrentUser UserInfo currentUser) {
        if (excelImportFile.isExcelNotEmpty()) {
        	organizationsExcelService.importFromExcel(excelImportFile,currentUser);
        }

        return new Message<>(Message.FAIL);

    }

    @GetMapping(value = "/export/{type}")
    public void exportOrganizations(@ModelAttribute Organizations organization,
                                    @PathVariable("type") String type,
                                    HttpServletResponse response,
                                    @CurrentUser UserInfo currentUser) {
    	organizationsExcelService.exportToExcel(type,organization,response);
    }

}
