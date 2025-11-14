/*
 * Copyright (c) 2024, MaxKey and/or its affiliates. All rights reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * License Restrictions
 * This software and related documentation are provided under a license
 * agreement containing restrictions on use and disclosure and are
 * protected by intellectual property laws. Except as expressly permitted
 * in your license agreement or allowed by law, you may not use, copy,
 * reproduce, translate, broadcast, modify, license, transmit, distribute,
 * exhibit, perform, publish, or display any part, in any form, or by any means.
 * Reverse engineering, disassembly, or decompilation of this software,
 * unless required by law for interoperability, is prohibited.
 *
 * Please contact MaxKey, visit www.maxkey.top if you need additional information
 * or have any questions,support email support@maxsso.net .
 *
 */


package com.surpass.authn.web;

import com.surpass.authn.SignedPrincipal;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;
import org.dromara.mybatis.jpa.handler.FieldAutoFillHandler;

@Component
public class PersistFieldAutoFillHandler extends FieldAutoFillHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        SignedPrincipal principal = getPrincipal();
        if(principal != null) {
            this.setFieldValue(metaObject , "instId", principal.getInstId());
            this.setFieldValue(metaObject , "createdBy", principal.getUserId());
        }
        this.setFieldValue(metaObject , "createdDate", new Date());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        SignedPrincipal principal = getPrincipal();
        if(principal != null) {
            this.setFieldValue(metaObject , "modifiedBy", principal.getUserId());
        }
        this.setFieldValue(metaObject , "modifiedDate", new Date());
    }

    /**
     * 获取principal , 忽略异常情况
     * @return
     */
    SignedPrincipal getPrincipal() {
        SignedPrincipal principal = null;
        try {
            principal = AuthorizationUtils.getPrincipal();
        }catch(Exception e) {
            //
        }
        return principal;
    }

}
