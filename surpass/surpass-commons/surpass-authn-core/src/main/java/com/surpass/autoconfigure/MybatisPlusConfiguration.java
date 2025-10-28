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


package com.surpass.autoconfigure;

import cn.hutool.core.net.NetUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.surpass.authn.web.PersistFieldAutoFillHandler;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * mybatis-plus配置类
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = "com.surpass.persistence.mapper")
public class MybatisPlusConfiguration {


    @Value("${mybatis-plus.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis-plus.type-aliases-package}")
    private String typeAliasesPackage;

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource, MybatisPlusInterceptor interceptor) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = null;
        // 实例SessionFactory
        sessionFactory = new MybatisSqlSessionFactoryBean();
        // 配置数据源
        sessionFactory.setDataSource(dataSource);
        // 加载MyBatis配置文件
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        // 能加载多个，所以可以配置通配符(如：classpath*:mapper/**/*.xml)
        sessionFactory.setMapperLocations(resourcePatternResolver.getResources(mapperLocations));
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);

        // 配置 MyBatis 配置对象
        MybatisConfiguration configuration = new MybatisConfiguration();

        // 2. 设置日志实现为 slf4j
        configuration.setLogImpl(Slf4jImpl.class);
        configuration.setMapUnderscoreToCamelCase(true);

        // 设置全局配置（MetaObjectHandler 会从这里读取）
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(metaObjectHandler());
        sessionFactory.setGlobalConfig(globalConfig);

        // 将配置设置到 SqlSessionFactoryBean 中
        sessionFactory.setConfiguration(configuration);

        // 设置分页插件
        sessionFactory.setPlugins(interceptor);

        return sessionFactory.getObject();
    }

    @Bean
    DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花ID重复
     */
    @Primary
    @Bean
    IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator(NetUtil.getLocalhost());
    }

    @Bean
    public PersistFieldAutoFillHandler metaObjectHandler() {
        return new PersistFieldAutoFillHandler();
    }
}
