package org.dromara.surpass.sureness.processor;

import com.usthe.sureness.processor.BaseProcessor;
import com.usthe.sureness.processor.exception.*;
import com.usthe.sureness.provider.SurenessAccount;
import com.usthe.sureness.provider.SurenessAccountProvider;
import com.usthe.sureness.subject.PrincipalMap;
import com.usthe.sureness.subject.Subject;
import com.usthe.sureness.subject.support.JwtSubject;
import com.usthe.sureness.subject.support.SinglePrincipalMap;
import com.usthe.sureness.util.JsonWebTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * custom token processor, support JwtSubject
 * when token Expired and can refresh, return refresh token value
 *
 * @author tomsun28
 * @date 2020-12-03 20:37
 */
public class RefreshJwtProcessor extends BaseProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RefreshJwtProcessor.class);
    private static final String TOKEN_SPLIT = "--";
    private static final int START_TIME_INDEX = 1;
    private static final int PERIOD_TIME_INDEX = 2;
    private static final int DOUBLE_TIME = 2;

    private SurenessAccountProvider accountProvider;

    @Override
    public boolean canSupportSubjectClass(Class<?> var) {
        return var == JwtSubject.class;
    }

    @Override
    public Class<?> getSupportSubjectClass() {
        return JwtSubject.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Subject authenticated(Subject var) throws SurenessAuthenticationException {
        String jwt = (String) var.getCredential();
        if (JsonWebTokenUtil.isNotJsonWebToken(jwt)) {
            throw new  IncorrectCredentialsException("this jwt credential is illegal");
        }
        Claims claims;
        try {
            claims = JsonWebTokenUtil.parseJwt(jwt);
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            // JWT error
            if (logger.isDebugEnabled()) {
                logger.debug("jwtProcessor authenticated fail, user: {}, jwt: {}",
                        var.getPrincipal(), jwt);
            }
            throw new IncorrectCredentialsException("this jwt error:" + e.getMessage());
        } catch (ExpiredJwtException e) {
            // JWT expired
            if (logger.isDebugEnabled()) {
                logger.debug("jwtProcessor authenticated expired, user: {}, jwt: {}",
                        var.getPrincipal(), jwt);
            }
            // token expired or not exist, if token can refresh, refresh it
            // if expired time is not longer than refreshPeriodTime/2 , it can refresh

            if (e.getClaims().getExpiration().getTime() - System.currentTimeMillis() <= 18000) {
                String refreshToken = JsonWebTokenUtil.issueJwt(UUID.randomUUID().toString(), e.getClaims().getSubject(),
                        "tom-auth-server", 1800L, (List<String>) e.getClaims().get("roles", List.class),
                        null, false);
                throw new RefreshExpiredTokenException(refreshToken);
            } else {
                throw new ExpiredCredentialsException("this jwt has expired");
            }
        }

        JwtSubject.Builder builder = JwtSubject.builder(var)
                .setPrincipal(claims.getSubject());
        List<String> ownRoles = claims.get("roles", List.class);
        if (ownRoles != null) {
            builder.setOwnRoles(ownRoles);
        }
        PrincipalMap principalMap = new SinglePrincipalMap();
        for (Map.Entry<String, Object> claimEntry : claims.entrySet()) {
            principalMap.setPrincipal(claimEntry.getKey(), claimEntry.getValue());
        }
        builder.setPrincipalMap(principalMap);
        return builder.build();
    }

}
