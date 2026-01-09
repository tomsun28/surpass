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




package org.maxkey.authn.secretkey;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.maxkey.surpass.authn.LoginSecretKey;
import org.maxkey.surpass.authn.secretkey.SecretKeyProvider;
import org.maxkey.surpass.crypto.SM2Utils;

public class SecretKeyProviderTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		SecretKeyProvider provider = new SecretKeyProvider(SecretKeyProvider.AlgorithmType.SM2, 24);
		LoginSecretKey secretKey = provider.generator();
		BCECPublicKey publicKeyDE = (BCECPublicKey)SM2Utils.publicKey(Hex.decodeHex(secretKey.getPublicKey()));
        BCECPrivateKey privateKeyDE = (BCECPrivateKey)SM2Utils.privateKey(Hex.decodeHex(secretKey.getPrivateKey()));

        String plainText = "maxkey";

        // 公钥加密
        byte[] encryptedData = SM2Utils.encrypt (plainText.getBytes (), publicKeyDE);
        String decryptedDataHex=Hex.encodeHexString (encryptedData);
        System.out.println ("公钥加密 数据: " + decryptedDataHex);

        // 私钥解密
        byte[] decryptedData = SM2Utils.decrypt (encryptedData, privateKeyDE);
        System.out.println ("私钥解密 数据: " + new String (decryptedData));

        System.out.println ("私钥解密provider 数据: " +new String (provider.decrypt(decryptedDataHex, secretKey.getPrivateKey())));
	}

}
