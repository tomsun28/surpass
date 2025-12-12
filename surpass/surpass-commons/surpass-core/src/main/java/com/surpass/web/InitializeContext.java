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






package com.surpass.web;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Iterator;

import javax.sql.DataSource;

import jakarta.servlet.http.HttpServlet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;

import com.surpass.configuration.ApplicationConfig;
import com.surpass.util.JdbcUtils;

/**
 * InitApplicationContext . 初始化应用上下文
 *
 * @author Crystal.Sea
 *
 */
public class InitializeContext extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(InitializeContext.class);
    private static final long serialVersionUID = -797399138268601444L;

    final ApplicationContext applicationContext;

    public static final String LOCALE_RESOLVER = "localeResolver";

    public static final String COOKIE_LOCALE_RESOLVER = "cookieLocaleResolver";

    public void init() {
    	// WebContext
    	WebContext.init(applicationContext);

        // List Properties
        listProperties();

        // List DatabaseMetaData Variables
        listDataBaseVariables();

    	// Show License
        showLicense();
    }

    /**
    * InitApplicationContext.
    */
    public InitializeContext(ConfigurableApplicationContext configurableApplicationContext) {
    	if(configurableApplicationContext.containsBean(LOCALE_RESOLVER) &&configurableApplicationContext.containsBean(COOKIE_LOCALE_RESOLVER)) {
            BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry)configurableApplicationContext.getBeanFactory();
            beanFactory.removeBeanDefinition(LOCALE_RESOLVER);
            beanFactory.registerBeanDefinition(LOCALE_RESOLVER, beanFactory.getBeanDefinition(COOKIE_LOCALE_RESOLVER));
            logger.debug("cookieLocaleResolver replaced localeResolver.");
        }
    	//
    	applicationContext = configurableApplicationContext;
    }

    /**
     * listDataBaseVariables.
     */
    public void listDataBaseVariables() {
        if (applicationContext.containsBean("dataSource")) {
        	Connection connection = null;
            try {
                logger.info(WebConstants.DELIMITER);
                logger.info("List DatabaseMetaData Variables ");
                connection = ((DataSource) applicationContext.getBean("dataSource")).getConnection();

                DatabaseMetaData databaseMetaData = connection.getMetaData();
                ApplicationConfig.setDatabaseProduct(databaseMetaData.getDatabaseProductName());

                logger.info("DatabaseProductName    :   {}", databaseMetaData.getDatabaseProductName());
                logger.info("DatabaseProductVersion :   {}" ,databaseMetaData.getDatabaseProductVersion());
                logger.trace("DatabaseMajorVersion  :   {}" ,databaseMetaData.getDatabaseMajorVersion());
                logger.trace("DatabaseMinorVersion  :   {}" ,databaseMetaData.getDatabaseMinorVersion());
                logger.trace("supportsTransactions  :   {}" ,databaseMetaData.supportsTransactions());
                logger.trace("DefaultTransaction    :   {}" ,databaseMetaData.getDefaultTransactionIsolation());
                logger.trace("MaxConnections        :   {}" ,databaseMetaData.getMaxConnections());
                logger.trace("");
                logger.trace("JDBCMajorVersion      :   {}" ,databaseMetaData.getJDBCMajorVersion());
                logger.trace("JDBCMinorVersion      :   {}" ,databaseMetaData.getJDBCMinorVersion());
                logger.trace("DriverName            :   {}" ,databaseMetaData.getDriverName());
                logger.trace("DriverVersion         :   {}" ,databaseMetaData.getDriverVersion());
                logger.info("");
                logger.info("DBMS  URL              :   {}" ,databaseMetaData.getURL());
                logger.info("UserName               :   {}" ,databaseMetaData.getUserName());
                logger.info(WebConstants.DELIMITER);
            } catch (SQLException e) {
                logger.error("DatabaseMetaData Variables Error .",e);
            }finally {
            	if(connection != null) {
            		JdbcUtils.release(connection);
            	}
            }
        }
    }

    /**
     * propertySourcesPlaceholderConfigurer.
     */
    public void listProperties() {
        if (applicationContext.containsBean("propertySourcesPlaceholderConfigurer")) {
            logger.trace(WebConstants.DELIMITER);
            logger.trace("List Properties Variables ");
            Iterator<PropertySource<?>> it = WebContext.getProperties().getPropertySources().iterator();
            while(it.hasNext()) {
            	 logger.debug("propertySource {}" , it.next());
            }
            logger.trace(WebConstants.DELIMITER);
        }
    }

    /**
     * showLicense.
     */
    public void showLicense() {
        logger.info(WebConstants.DELIMITER);
        logger.info("+                      Surpass   Community  Edition  ");
        logger.info("+                           OpenAPI   Software       ");
        logger.info("+                           Version {}",
                        WebContext.getProperty("application.formatted-version"));
        logger.info("+");
        logger.info("+                 {}Copyright 2024 - {} https://www.surpass.com/",
        			    (char)0xA9 , new DateTime().getYear()
        			);
        logger.info("+                 .         All rights reserved         . ");
        logger.info(WebConstants.DELIMITER);
    }

}
