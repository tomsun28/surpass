package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.Institutions;


public interface InstitutionsService extends IJpaService<Institutions> {
    Institutions getByInstIdOrDomain(String instIdOrDomain);
}
