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






package org.dromara.surpass.crypto.jwt;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.util.Base64URL;

public class Hmac512Service {
	private static final Logger logger = LoggerFactory.getLogger(Hmac512Service.class);

	public static final  String MXK_AUTH_JWK = "mxk_auth_jwk";

	JWSSigner signer;

	MACVerifier verifier;

	public Hmac512Service() {
		super();
	}

	public Hmac512Service(String secretString) throws JOSEException {
		Base64URL secret= new Base64URL(secretString);
		OctetSequenceKey octKey=  new OctetSequenceKey.Builder(secret)
				.keyID(MXK_AUTH_JWK)
				.keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.HS512)
				.build();
		signer = new MACSigner(octKey);
		verifier = new MACVerifier(octKey);
	}

	public String sign(Payload payload) {
		try {
			// Prepare JWS object with payload HS512
			JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), payload);
			// Apply the HMAC
			jwsObject.sign(signer);
			return jwsObject.serialize();
		} catch (JOSEException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String sign(String  payload) {
		return sign(new Payload(payload));
	}


	public boolean verify(String jwt) {
		try {
		JWSObject jwsObjected =JWSObject.parse(jwt);
		return verifier.verify(
								jwsObjected.getHeader(),
								jwsObjected.getSigningInput(),
								jwsObjected.getSignature());
		}catch(JOSEException joseException) {
			logger.debug("JOSEException {}",joseException.getMessage());
		}catch(ParseException parseException) {
			logger.debug("ParseException {}",parseException.getMessage());
		}
		return false;
	}
}
