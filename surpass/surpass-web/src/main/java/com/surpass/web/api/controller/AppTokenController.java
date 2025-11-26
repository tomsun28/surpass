package com.surpass.web.api.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import com.surpass.authn.jwt.service.AuthTokenService;
import com.surpass.entity.Message;
import com.surpass.entity.app.App;
import com.surpass.persistence.service.AppService;
import com.surpass.security.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/26 15:29
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppTokenController {

    private final AppService appService;

    private final TokenStore tokenStore;

    @PostMapping("/token")
    public Message<Map<String, Object>> getToken(@RequestParam String clientId,
                                                        @RequestParam String clientSecret) {

        App app = appService.findByClientId(clientId);
        if(app == null || !BCrypt.checkpw(clientSecret, app.getClientSecret())){
            return new Message<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        String token = appService.issueToken(app);

        Map<String,Object> result = new HashMap<>();
        result.put("access_token", token);
        result.put("token_type", "Bearer");
        result.put("expires_in", app.getTtlSeconds());
        return Message.ok(result);
    }
}
