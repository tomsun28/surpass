/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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






package org.dromara.surpass.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
public class LoginConfig {

    @Value("${jinbooks.login.jwt.issuer:https://www.jinbooks.com}")
    String jwtIssuer;

    //Iplocation
    @Value("${jinbooks.login.iplocation:false}")
    boolean isIplocation;

    @Value("${jinbooks.login.iplocation.online.provider:none}")
    String locationOnlineProvider;

    @Value("${jinbooks.login.iplocation.offline.provider:none}")
    String locationOfflineProvider;

    @Value("${jinbooks.login.cas.serverUrlPrefix:https://www.jinbooks.com/sign/authz/cas}")
    String casServerUrlPrefix;

    @Value("${jinbooks.login.cas.service:https://www.jinbooks.com/passport/trust/auth}")
    String casService;
}
