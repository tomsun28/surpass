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






package org.dromara.surpass.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class HmacSha256 {

	public static final String HMAC_ALGORITHM = "HmacSHA256";

	public static byte[]  encode(String message, String secret)
			throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        hmac.init(key);

        return hmac.doFinal(message.getBytes());
    }

	public static String encodeBase64(String message, String secret)
			throws NoSuchAlgorithmException, InvalidKeyException {
        return Base64.encodeBase64String(encode(message,secret));
    }

	public static String encodeHex(String message, String secret)
			throws NoSuchAlgorithmException, InvalidKeyException {
        return HexUtils.bytes2HexString(encode(message,secret));
    }

    public static boolean verify(String message, String secret, byte[] hmac)
    		throws InvalidKeyException, NoSuchAlgorithmException {
    	String hmacB64 =Base64.encodeBase64String(hmac);
    	String hmacEncodeB64 =Base64.encodeBase64String(encode(message, secret));
        return hmacB64.equals(hmacEncodeB64);
    }



    public static boolean verifyHex(String message, String secret, String hmacHex)
    		throws InvalidKeyException, NoSuchAlgorithmException {
        return hmacHex.equals(encodeHex(message, secret));
    }

    public static boolean verifyBase64(String message, String secret, String hmacB64)
    		throws InvalidKeyException, NoSuchAlgorithmException {
        return hmacB64.equals(encodeBase64(message, secret));
    }

}
