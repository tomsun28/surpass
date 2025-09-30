package org.dromara.surpass.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.surpass.dao.InstitutionsDao;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.pojo.entity.Institutions;
import org.dromara.surpass.service.InstitutionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/30 11:02
 */

@Service
@RequiredArgsConstructor
public class InstitutionsServiceImpl extends JpaServiceImpl<InstitutionsDao, Institutions> implements InstitutionsService {
    private static final Logger logger = LoggerFactory.getLogger(InstitutionsServiceImpl.class);

    private final InstitutionsDao institutionsDao;

    protected static final Cache<String, Institutions> institutionsStore =
            Caffeine.newBuilder()
                    .expireAfterWrite(60, TimeUnit.MINUTES)
                    .build();

    //id to domain mapping
    protected static final ConcurrentHashMap<String,String> mapper = new ConcurrentHashMap<>();

    private static final String DEFAULT_INSTID = "1";


    @Override
    public Institutions getByInstIdOrDomain(String instIdOrDomain) {
        logger.trace(" instId or domain {}" , instIdOrDomain);
        Institutions inst = institutionsStore.getIfPresent(mapper.get(instIdOrDomain)== null ? DEFAULT_INSTID : mapper.get(instIdOrDomain) );
        if(inst == null) {
            inst = institutionsDao.getByInstIdOrDomain(instIdOrDomain);
            if(inst != null ) {
                institutionsStore.put(inst.getDomain(), inst);
                mapper.put(inst.getId(), inst.getDomain());
            }
        }
        if(inst == null) {//use default inst
            inst = getByInstIdOrDomain(DEFAULT_INSTID);
            institutionsStore.put(instIdOrDomain, inst);
            mapper.put(instIdOrDomain, inst.getDomain());
        }
        return inst;
    }
}
