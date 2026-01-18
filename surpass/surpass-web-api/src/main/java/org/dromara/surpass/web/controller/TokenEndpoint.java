package org.dromara.surpass.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.authn.token.AccessToken;
import org.dromara.surpass.authn.token.TokenManager;
import org.dromara.surpass.constants.ConstsToken;
import org.dromara.surpass.crypto.password.PasswordReciprocal;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.RegisteredClient;
import org.dromara.surpass.persistence.service.RegisteredClientService;
import org.dromara.surpass.web.WebContext;
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
	
	private final PasswordReciprocal passwordReciprocal;
	
    private final RegisteredClientService clientService;
    
    @PostMapping("/token")
    public Message<AccessToken> getToken(
    			@RequestParam(ConstsToken.CLIENT_ID) String clientId,
    			@RequestParam(ConstsToken.CLIENT_SECRET) String clientSecret,
    			@RequestParam(name=ConstsToken.GRANT_TYPE,required=false) String grantType,
    			@RequestParam(name=ConstsToken.REFRESH_TOKEN,required=false) String refreshToken,
    			HttpServletRequest request) {
    	RegisteredClient client = clientService.findByClientId(clientId);
    	Message<AccessToken> message = new Message<>(Message.FAIL);
    	
    	if(client == null) {
    		message.setMessage("invalid_client_id");
    	}else if(StringUtils.isBlank(client.getIpWhiteList())) {//未设置ip白名单
    		grantToken(clientId,clientSecret,grantType,refreshToken,client,message);
    	}else if(StringUtils.isNotBlank(client.getIpWhiteList())) {//设置ip白名单
    		String ipAddr = WebContext.getRequestIpAddress(request);
    		if(client.getIpAddrSet().contains(ipAddr)) {
    			grantToken(clientId,clientSecret,grantType,refreshToken,client,message);
    		}else {
    			message.setMessage("invalid_ipaddress_"+ipAddr);
    		}
    	}
    	if(message.getCode() == Message.SUCCESS) {
    		client.setLastLoginTime(new Date());
    		clientService.updateLastLoginTime(client);
    	}
    	log.debug("accessToken {}",message);
        return message;
    }
    
    void grantToken(String clientId , String clientSecret,String grantType,String refreshToken,RegisteredClient client,Message<AccessToken> message){
    	if(passwordReciprocal.matches(clientSecret, client.getClientSecret())){
    		if(StringUtils.equalsIgnoreCase("refresh_token", grantType)) {
        		String refreshTokenDecoder = null;
        		try{
        			refreshTokenDecoder = passwordReciprocal.decoder(refreshToken);
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
    	}else {
    		message.setMessage("invalid_client_secret");
    	}
    }
}
