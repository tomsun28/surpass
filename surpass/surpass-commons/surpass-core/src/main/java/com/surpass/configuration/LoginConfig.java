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

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Configuration
public class LoginConfig {

    @Value("${surpass.login.jwt.issuer:https://www.surpass.com}")
    String jwtIssuer;

    //Iplocation
    @Value("${surpass.login.iplocation:false}")
    boolean isIplocation;

    @Value("${surpass.login.iplocation.online.provider:none}")
    String locationOnlineProvider;

    @Value("${surpass.login.iplocation.offline.provider:none}")
    String locationOfflineProvider;

    @Value("${surpass.login.cas.serverUrlPrefix:https://www.surpass.com/sign/authz/cas}")
    String casServerUrlPrefix;

    @Value("${surpass.login.cas.service:https://www.surpass.com/passport/trust/auth}")
    String casService;
}
