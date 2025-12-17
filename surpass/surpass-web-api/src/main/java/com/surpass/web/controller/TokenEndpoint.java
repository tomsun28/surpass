package com.surpass.web.controller;

import com.surpass.authn.token.AccessToken;
import com.surpass.authn.token.TokenManager;
import com.surpass.constants.ConstsToken;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.entity.Message;
import com.surpass.entity.RegisteredClient;
import com.surpass.persistence.service.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.Logger;
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

	private final TokenManager tokenManager;
	
    private final RegisteredClientService clientService;
    
    @PostMapping("/token")
    public Message<AccessToken> getToken(
    			@RequestParam(ConstsToken.CLIENT_ID) String clientId,
    			@RequestParam(ConstsToken.CLIENT_SECRET) String clientSecret,
    			@RequestParam(ConstsToken.GRANT_TYPE) String grantType,
    			@RequestParam(ConstsToken.REFRESH_TOKEN) String refreshToken) {
    	RegisteredClient client = clientService.findByClientId(clientId);
    	Message<AccessToken> message = new Message<>(Message.FAIL);
    	
    	if(client == null) {
    		message.setMessage("invalid_client_id");
    	}else if(StringUtils.equalsIgnoreCase("refresh_token", grantType)) {
    		String refreshTokenDecoder = null;
    		try{
    			refreshTokenDecoder = PasswordReciprocal.getInstance().decoder(refreshToken);
    		}catch(Exception e) {
    			log.error("decoder refresh_token failed.");
    		}
    		message.setMessage("invalid_refresh_token");
    		if(StringUtils.isNotBlank(refreshTokenDecoder)) {
    			AccessToken accessToken = tokenManager.refresh(refreshTokenDecoder);
    			if(accessToken != null) {
	    			message.setData(accessToken);
	        		message.setCode(Message.SUCCESS);
	        		message.setMessage("");
    			}
    		}
    	}else {
    		AccessToken accessToken = tokenManager.generate(clientId);
    		message.setData(accessToken);
    		message.setCode(Message.SUCCESS);
    	}
        return message;
    }
}
