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




package org.maxkey.surpass.authn.secretkey;

import java.security.KeyPair;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.maxkey.surpass.authn.LoginSecretKey;
import org.maxkey.surpass.crypto.Base64Utils;
import org.maxkey.surpass.crypto.RSAUtils;
import org.maxkey.surpass.crypto.SM2Utils;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecretKeyProvider {
	private static final Logger logger = LoggerFactory.getLogger(SecretKeyProvider.class);

	/**
	 * SM2 OR RSA
	 */
	String algorithm = AlgorithmType.SM2;

	int expiresHours;

	public final class AlgorithmType {

	    public static final String SM2 			= "SM2";

	    public static final String RSA 			= "RSA";

	}

	public SecretKeyProvider(String algorithm,int expiresHours) {
		super();
		this.expiresHours = expiresHours;
		this.algorithm = algorithm;
	}

	public LoginSecretKey generator() throws Exception{
		LoginSecretKey loginSecretKey = null;
		logger.info("algorithm {}",algorithm);
		if(algorithm.equalsIgnoreCase(AlgorithmType.RSA)) {
			KeyPair  keyPair  = RSAUtils.genRSAKeyPair();
			String   privateKey = Base64Utils.encoder(keyPair.getPrivate().getEncoded());
			String   publicKey  = Base64Utils.encoder(keyPair.getPublic().getEncoded());
			loginSecretKey = new LoginSecretKey(WebContext.genId(),publicKey,privateKey,expiresHours);
		}else if(algorithm.equalsIgnoreCase(AlgorithmType.SM2)) {
			while(loginSecretKey == null) {
				try {
					KeyPair  keyPair  =SM2Utils.generateKeyPair();
					BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic ();
			        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate ();
			        //私钥Hex格式，必须64位
					String privateKeyHex = privateKey.getD().toString(16);
					//公钥Hex格式，04开头，130位
					String publicKeyHex = Hex.encodeHexString(publicKey.getQ().getEncoded(false));


					//just test
					BCECPublicKey publicKeyDE = (BCECPublicKey)SM2Utils.publicKey(Hex.decodeHex(publicKeyHex));
			        BCECPrivateKey privateKeyDE = (BCECPrivateKey)SM2Utils.privateKey(Hex.decodeHex(privateKeyHex));
			        String plainText = SecretKeyProvider.class.getSimpleName();
			        // 公钥加密
			        byte[] encryptedData = SM2Utils.encrypt (plainText.getBytes (), publicKeyDE);
			        String decryptedDataHex=Hex.encodeHexString (encryptedData);
			        logger.debug ("publicKey encrypt hex data : {}" , decryptedDataHex);
			        // 私钥解密
			        byte[] decryptedData = SM2Utils.decrypt (encryptedData, privateKeyDE);
			        logger.debug ("privateKey decrypt hex data {}: " , new String (decryptedData));

			        loginSecretKey = new LoginSecretKey(WebContext.genId(),publicKeyHex,privateKeyHex,expiresHours);
				}catch(Exception e){}
			}
		}

		return loginSecretKey;
	}

	public byte[] decrypt(String cipherText,String privateKey) throws Exception{
		byte[] decrypt = null;
		if(algorithm.equalsIgnoreCase(AlgorithmType.RSA)) {
			decrypt = RSAUtils.decryptByPrivateKey(Base64Utils.decoder(cipherText), Base64Utils.decoder(privateKey));
		}else if(algorithm.equalsIgnoreCase(AlgorithmType.SM2)) {
			BCECPrivateKey bcecPrivateKey = (BCECPrivateKey)SM2Utils.privateKey(Hex.decodeHex(privateKey));
			//使用BC库加解密时密文以04开头，传入的密文前面没有04则补上
			if(!cipherText.startsWith("04")) {
				cipherText = "04" + cipherText ;
			}
			decrypt = SM2Utils.decrypt(Hex.decodeHex(cipherText), bcecPrivateKey);
		}
		return decrypt;
	}
}
