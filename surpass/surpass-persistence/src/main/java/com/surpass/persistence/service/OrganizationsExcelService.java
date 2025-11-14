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


package com.surpass.persistence.service;

import jakarta.servlet.http.HttpServletResponse;
import com.surpass.entity.ExcelImport;
import com.surpass.entity.idm.Organizations;
import com.surpass.entity.idm.UserInfo;
import org.dromara.mybatis.jpa.service.IJpaService;

public interface OrganizationsExcelService  extends IJpaService<Organizations> {
    void importFromExcel(ExcelImport excelImportFile, UserInfo currentUser) ;

    void exportToExcel(String exportType,Organizations organization, HttpServletResponse response) ;
}
