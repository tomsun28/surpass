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




package org.dromara.crypto;

import java.security.KeyPair;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.dromara.surpass.crypto.SM2Utils;

public class SM2UtilsTest {

	//https://blog.csdn.net/zte1055889498/article/details/132489094
	public static void main(String[] args) throws Exception {
		  // 生成SM2密钥对
        KeyPair keyPair = SM2Utils.generateKeyPair ();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic ();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate ();

        String privateKeyHex = privateKey.getD().toString(16);
        System.out.println("privateKeyHex "+privateKeyHex);

        String publicKeyHex = Hex.encodeHexString(publicKey.getQ().getEncoded(false));
        System.out.println("publicKeyHex "+publicKeyHex);

        BCECPublicKey publicKeyDE = (BCECPublicKey)SM2Utils.publicKey(Hex.decodeHex(publicKeyHex));
        BCECPrivateKey privateKeyDE = (BCECPrivateKey)SM2Utils.privateKey(Hex.decodeHex(privateKeyHex));

        String plainText = "你好, SM2!";


        // 公钥加密
        byte[] encryptedData = SM2Utils.encrypt (plainText.getBytes (), publicKeyDE);
        System.out.println ("公钥加密 数据: " + Hex.encodeHexString (encryptedData));

        // 私钥解密
        byte[] decryptedData = SM2Utils.decrypt (encryptedData, privateKeyDE);
        System.out.println ("私钥解密 数据: " + new String (decryptedData));

        // 私钥签名
        byte[] signature = SM2Utils.sign (plainText.getBytes (), privateKeyDE);
        System.out.println ("私钥签名数据: " + Hex.encodeHexString (signature));

        // 公钥验签
        boolean isVerified = SM2Utils.verify (plainText.getBytes (), signature, publicKeyDE);
        System.out.println ("公钥验签 数据: " + isVerified);
	}

}
