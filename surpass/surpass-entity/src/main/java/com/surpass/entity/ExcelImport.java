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




package com.surpass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;
import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * .
 *
 * @author Crystal.Sea
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ExcelImport extends BaseEntity {

    /**
	 *
	 */
	private static final long serialVersionUID = -2764463764570801771L;
	String id;
    @JsonIgnore
    protected MultipartFile excelFile;
    String updateExist;
    InputStream inputStream = null;
    Workbook workbook = null;

    public boolean isExcelNotEmpty() {
        return excelFile != null && !excelFile.isEmpty();
    }


    public Workbook biuldWorkbook() throws IOException {
        workbook = null;
        inputStream = excelFile.getInputStream();
        if (excelFile.getOriginalFilename().toLowerCase().endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (excelFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new RuntimeException("Excel suffix error.");
        }
        return workbook;
    }

    public void closeWorkbook() {
//        if (inputStream != null) {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                log.error(e.getMessage(), e);
//            }
//        }
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
