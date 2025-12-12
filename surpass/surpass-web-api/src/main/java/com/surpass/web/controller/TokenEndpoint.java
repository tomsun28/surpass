package com.surpass.web.controller;

import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.persistence.service.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/26 15:29
 */

@RestController
@RequiredArgsConstructor
public class TokenEndpoint {

    private final RegisteredClientService clientService;

    @PostMapping("/token")
    public Message<Map<String, Object>> getToken(@RequestParam String clientId,
                                                 @RequestParam String clientSecret) {

    /*    App app = appService.findByClientId(clientId);
        if(app == null || !BCrypt.checkpw(clientSecret, app.getClientSecret())){
            return new Message<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        String token = appService.issueToken(app);*/
    	RegisteredClient client = clientService.findByClientId(clientId);

        Map<String,Object> result = new HashMap<>();
//        result.put("access_token", token);
        result.put("token_type", "Bearer");
//        result.put("expires_in", app.getTtlSeconds());
        return Message.ok(result);
    }
}
