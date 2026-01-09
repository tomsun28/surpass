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


package org.maxkey.surpass.persistence.service;
import org.dromara.hutool.core.tree.MapTree;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.idm.Organizations;
import org.maxkey.surpass.entity.idm.dto.OrgPageDto;

import java.util.List;

public interface OrganizationsService extends IJpaService<Organizations> {
    List<MapTree<String>> tree(Organizations org);

    Message<JpaPageResults<Organizations>> pageList(OrgPageDto dto);

    boolean updateOneOrg(Organizations organizations);

    boolean saveOneOrg(Organizations organizations);
}
