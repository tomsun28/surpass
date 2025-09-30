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






package org.dromara.surpass.authn.jwt.service;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.authn.SignedPrincipal;
import org.dromara.surpass.constants.ConstsJwt;
import org.dromara.surpass.crypto.jwt.Hmac512Service;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.web.WebContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.util.Date;

/**
 * 认证JWT生成服务
 *
 * @author Crystal.Sea
 *
 */
public class AuthJwtService {
	private static final  Logger logger = LoggerFactory.getLogger(AuthJwtService.class);

	/**
	 * HMAC512签名服务
	 */
	Hmac512Service hmac512Service;

	/**
	 * JWT with Authentication/根据认证信息生成JWT
	 * @param authentication
	 * @return
	 */
	public String genJwt(Authentication authentication,String issuer,int expires) {
		SignedPrincipal principal = ((SignedPrincipal)authentication.getPrincipal());
		UserInfo userInfo = principal.getUserInfo();
		DateTime currentDateTime = DateTime.now();
		String subject = principal.getUsername();
		Date expirationTime = currentDateTime.plusSeconds(expires).toDate();
		logger.trace("jwt subject : {} , expiration Time : {}" , subject,expirationTime);
		 JWTClaimsSet jwtClaims =new  JWTClaimsSet.Builder()
				.issuer(issuer)
				.subject(subject)
				.jwtID(principal.getSessionId())
				.issueTime(currentDateTime.toDate())
				.expirationTime(expirationTime)
				.claim(ConstsJwt.KID, Hmac512Service.MXK_AUTH_JWK)
				.claim(ConstsJwt.TWO_FACTOR, principal.getTwoFactor())
				.claim(ConstsJwt.USER_ID, userInfo.getId())
				.claim(ConstsJwt.INST_ID, userInfo.getInstId())
				.claim(ConstsJwt.LOCALE, userInfo.getLocale())
				.build();

		return signedJWT(jwtClaims);
	}

	/**
	 * JWT with subject/根据subject , issuer , expires生成JWT
	 * @param subject subject
	 * @return
	 */
	public String genJwt(String subject,String issuer,int expires) {
		DateTime currentDateTime = DateTime.now();
		Date expirationTime = currentDateTime.plusSeconds(expires).toDate();
		logger.trace("jwt subject : {} , expiration Time : {}" , subject,expirationTime);

		 JWTClaimsSet jwtClaims =new  JWTClaimsSet.Builder()
				.issuer(issuer)
				.subject(subject)
				.jwtID(WebContext.genId())
				.issueTime(currentDateTime.toDate())
				.expirationTime(expirationTime)
				.build();

		return signedJWT(jwtClaims);
	}

	/**
	 * Random JWT/随机生成JWT
	 * @return
	 */
	public String genRandomJwt(int expires) {
		Date expirationTime = DateTime.now().plusSeconds(expires).toDate();
		logger.trace("expiration Time : {}" , expirationTime);

		 JWTClaimsSet jwtClaims =new  JWTClaimsSet.Builder()
				.jwtID(WebContext.genId())
				.expirationTime(expirationTime)
				.build();

		return signedJWT(jwtClaims);
	}

	/**
	 * JWT HS512签名claims数据
	 * @param jwtClaims
	 * @return jwt string
	 */
	public String signedJWT(JWTClaimsSet jwtClaims) {
		logger.trace("jwt Claims : {}" , jwtClaims);
		SignedJWT  jwtToken = new SignedJWT(
				new JWSHeader(JWSAlgorithm.HS512),
				jwtClaims);
		return hmac512Service.sign(jwtToken.getPayload());
	}

	/**
	 *
	 * Verify with HMAC512 and check ExpirationTime
	 * <p>使用HMAC512验证JWT并检查是否过期</p>
	 *
	 * @param authToken
	 * @return true or false
	 */
	public boolean validateJwtToken(String authToken) {
		if(StringUtils.isNotBlank(authToken) && authToken.length() > 50) {
			try {
				JWTClaimsSet claims = resolve(authToken);
				boolean isExpiration = claims.getExpirationTime().after(DateTime.now().toDate());
				boolean isVerify = hmac512Service.verify(authToken);
				boolean isValidate = isVerify && isExpiration;
				logger.debug("JWT Validate {} , HMAC Verify {} , now {} , Expiration at {} , Expiration Time is Validate : {}" ,
						isValidate,isVerify,DateTime.now().toDate(),claims.getExpirationTime(),isExpiration);
				return isValidate;
			} catch (ParseException e) {
				logger.error("authToken {}",authToken,e);
			}
		}
		return false;
	}

	/**
	 * 解析JWT的Claims字段
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public  JWTClaimsSet resolve(String authToken) throws ParseException {
		SignedJWT signedJWT = SignedJWT.parse(authToken);
		logger.trace("jwt Claims : {}" , signedJWT.getJWTClaimsSet());
		return signedJWT.getJWTClaimsSet();
	}

	/**
	 * 解析JWTID
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public String resolveJWTID(String authToken) throws ParseException {
		JWTClaimsSet claims = resolve(authToken);
		return claims.getJWTID();
	}
}
