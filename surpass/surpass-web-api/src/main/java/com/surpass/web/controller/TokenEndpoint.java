package com.surpass.web.controller;

import com.surpass.constants.ConstsToken;
import com.surpass.entity.AccessToken;
import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.persistence.service.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/26 15:29
 * @author shimingxy modify
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenEndpoint {

    private final RegisteredClientService clientService;

    @PostMapping("/token")
    public AccessToken getToken(
    			@RequestParam(ConstsToken.CLIENT_ID) String clientId,
    			@RequestParam(ConstsToken.CLIENT_SECRET) String clientSecret,
    			@RequestParam(ConstsToken.GRANT_TYPE) String grantType,
    			@RequestParam(ConstsToken.REFRESH_TOKEN) String refreshToken) {
    	RegisteredClient client = clientService.findByClientId(clientId);
    	
    	AccessToken accessToken = new AccessToken();
    	if(client == null) {
    		accessToken.setMessage(Message.FAIL ,"invalid_client_id");
    		
    	}
        return accessToken;
    }
}
