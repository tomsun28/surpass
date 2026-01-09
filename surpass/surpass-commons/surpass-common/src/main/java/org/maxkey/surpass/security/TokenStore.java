package org.maxkey.surpass.security;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/26 15:30
 */

@Component
public class TokenStore {
    private final Map<String, LocalDateTime> tokens = new ConcurrentHashMap<>();

    public void storeToken(String token, String clientId, Duration ttl){
        tokens.put(token, LocalDateTime.now().plus(ttl));
    }

    public boolean isValid(String token){
        LocalDateTime expire = tokens.get(token);
        if(expire == null) return false;
        if(LocalDateTime.now().isAfter(expire)){
            tokens.remove(token);
            return false;
        }
        return true;
    }

    public void revoke(String token){
        tokens.remove(token);
    }
}
