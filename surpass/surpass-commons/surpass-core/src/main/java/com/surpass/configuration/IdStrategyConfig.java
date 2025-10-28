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
 

 
package com.surpass.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
@Configuration
public class IdStrategyConfig {
	
    @Value("${surpass.id.strategy:SnowFlake}") 
    String strategy;
    
    @Value("${surpass.id.datacenterId:0}") 
    int datacenterId;
    
    @Value("${surpass.id.machineId:0}") 
    int machineId;
}
