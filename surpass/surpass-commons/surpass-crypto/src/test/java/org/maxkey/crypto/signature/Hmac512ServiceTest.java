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






package org.maxkey.crypto.signature;

import java.text.ParseException;
import java.util.Date;

import org.maxkey.surpass.crypto.Base64Utils;
import org.maxkey.surpass.crypto.jwt.Hmac512Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class Hmac512ServiceTest {
	private static final  Logger logger = LoggerFactory.getLogger(Hmac512ServiceTest.class);

	public static void main(String[] args) throws JOSEException, ParseException {
      String key ="7heM-14BtxjyKPuH3ITIm7q2-ps5MuBirWCsrrdbzzSAOuSPrbQYiaJ54AeA0uH2XdkYy3hHAkTFIsieGkyqxOJZ_dQzrCbaYISH9rhUZAKYx8tUY0wkE4ArOC6LqHDJarR6UIcMsARakK9U4dhoOPO1cj74XytemI-w6ACYfzRUn_Rn4e-CQMcnD1C56oNEukwalf06xVgXl41h6K8IBEzLVod58y_VfvFn-NGWpNG0fy_Qxng6dg8Dgva2DobvzMN2eejHGLGB-x809MvC4zbG7CKNVlcrzMYDt2Gt2sOVDrt2l9YqJNfgaLFjrOEVw5cuXemGkX1MvHj6TAsbLg";
      Base64URL secret=Base64URL.from(key);

      System.out.println("key");

      System.out.println( Base64Utils.encoder(secret.decode()));

      Hmac512Service hmac512Service = new Hmac512Service(key);
      String sign = hmac512Service.sign("{\"aud\":\"admin\",\"sub\":\"admin\",\"exp\":1661802002}");
      System.out.println("sign text");
      System.out.println(sign);
      //sign ="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImluc3RpdHV0aW9uIjoiMSIsImtpZCI6Im14a19hdXRoX2p3ayIsImlzcyI6Imh0dHA6XC9cL3Nzby5tYXhrZXkudG9wOjk1MjZcL21heGtleS1tZ3QtYXBpIiwiZXhwIjoxNjU4MTQwNjE5LCJsb2NhbGUiOiJkZSIsImlhdCI6MTY1ODEzOTcxOSwianRpIjoiNzQ2NDY5MzAzNTEwODkyNTQ0In0.v8i_WmZlASIcbpBhbByJSch7eGhmOh5fPG-1d605K2I";
      boolean isverify = hmac512Service.verify(sign);
      System.out.println("verify sign");
      System.out.println(isverify);
      resolve(sign);


      System.out.println("jwt verify");

      sign ="eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhZG1pbiIsInN1YiI6ImFkbWluIiwiZXhwIjoxNjYxODAyMDAyfQ.6N-wEZLZMDjSHfr2ziR960Y_I4G8IgurvUMK3r99gd0";
      System.out.println(sign);
      System.out.println(hmac512Service.verify(sign));
      resolve(sign);

      //

      System.out.println("jwt test");
      String issuer="admin";
      String aud = "admin";
      String subject= "admin";

      JWTClaimsSet jwtClaims =new  JWTClaimsSet.Builder()
				.issuer(issuer)
    		  	.audience(aud)
				.subject(subject)
				//.issueTime(currentDateTime.toDate())
				.expirationTime(new Date(1661802002251L))
				.build();

      SignedJWT  jwtToken = new SignedJWT(
				new JWSHeader(JWSAlgorithm.HS256),
				jwtClaims);

      System.out.println("Payload "+jwtToken.getPayload());
      sign = hmac512Service.sign(jwtToken.getPayload());
      System.out.println("sign " + sign);
	}


	public static JWTClaimsSet resolve(String authToken) throws ParseException {
		SignedJWT signedJWT = SignedJWT.parse(authToken);
		JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
		logger.debug("jwt Claims : {}" , claimsSet);
		logger.debug("jwt ExpirationTime : {}" , claimsSet.getExpirationTime());
		return claimsSet;
	}

}
