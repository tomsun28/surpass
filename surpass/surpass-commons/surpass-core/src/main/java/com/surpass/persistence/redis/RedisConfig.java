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





package com.surpass.persistence.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
@Configuration
public class RedisConfig {

    @Value("#{'${spring.redis.cluster.nodes}'.split(',')}")
    List<String> 	nodes;

    @Value("${spring.redis.host}")
    String 			hostName;

    @Value("${spring.redis.port:6379}")
    int 			port;

    @Value("${spring.redis.password}")
    String 			password;

    @Value("${spring.redis.timeout:10000}")
    int 			timeOut;

    @Value("${spring.redis.jedis.pool.max-idle:100}")
    int 			maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    int 			minIdle;

    @Value("${spring.redis.lettuce.pool.max-active:-1}")
    int 			maxActive;

    @Value("${spring.redis.jedis.pool.max-wait:1000}")
    int 			maxWait;

}
