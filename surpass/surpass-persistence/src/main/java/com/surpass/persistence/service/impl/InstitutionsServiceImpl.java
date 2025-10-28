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






package com.surpass.persistence.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.dto.InstitutionsPageDto;
import com.surpass.persistence.mapper.BookMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.surpass.entity.Institutions;
import com.surpass.persistence.mapper.InstitutionsMapper;
import com.surpass.persistence.service.InstitutionsService;

import org.springframework.stereotype.Service;


@Service
public class InstitutionsServiceImpl extends ServiceImpl<InstitutionsMapper,Institutions> implements InstitutionsService {
	private static final Logger logger = LoggerFactory.getLogger(InstitutionsServiceImpl.class);

	@Autowired
	InstitutionsMapper institutionsMapper;

	@Autowired
	BookMapper bookMapper;

    protected static final Cache<String, Institutions> institutionsStore =
            Caffeine.newBuilder()
                	.expireAfterWrite(60, TimeUnit.MINUTES)
                	.build();

    //id to domain mapping
    protected static final  ConcurrentHashMap<String,String> mapper = new ConcurrentHashMap<>();

    private static final String DEFAULT_INSTID = "1";

	public InstitutionsMapper getMapper() {
		return institutionsMapper;
	}

	 public Institutions getByInstIdOrDomain(String instIdOrDomain) {
		 logger.trace(" instId or domain {}" , instIdOrDomain);
		 Institutions inst = institutionsStore.getIfPresent(mapper.get(instIdOrDomain)== null ? DEFAULT_INSTID : mapper.get(instIdOrDomain) );
		 if(inst == null) {
	        inst = getMapper().getByInstIdOrDomain(instIdOrDomain);
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

	@Override
	public Page<Institutions> fetch(InstitutionsPageDto dto) {
        return institutionsMapper.fetch(dto.build(), dto);
	}

	@Override
	public Message<Page<Book>> getInstBooks(InstitutionsPageDto dto) {
		LambdaUpdateWrapper<Book> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(Book::getInstId, dto.getId());
		if (StringUtils.isNotBlank(dto.getBookName())) {
			wrapper.like(Book::getName, dto.getBookName());
		}
		Page<Book> bookPage = bookMapper.selectPage(dto.build(), wrapper);
		return Message.ok(bookPage);
	}
}
