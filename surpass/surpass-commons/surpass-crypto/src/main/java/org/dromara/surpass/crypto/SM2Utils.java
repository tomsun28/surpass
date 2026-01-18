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

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.Cipher;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

public class SM2Utils {

	static final String EC				= "EC";

	static final String ALGORITHM 		= "SM3withSM2";

	static final String TRANSFORMATION 	= "SM2";

	static {
        Security.addProvider (new BouncyCastleProvider ());
    }

	   /**
     * 生成SM2密钥对
     *
     * @return
     * @throws Exception
     */
    public static KeyPair generateKeyPair () throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance ("EC", new BouncyCastleProvider());
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec ("sm2p256v1");
        keyPairGenerator.initialize (ecGenParameterSpec, new SecureRandom ());
        return keyPairGenerator.generateKeyPair ();
    }

    public static PrivateKey privateKey(byte[] keyBytes) {
    	X9ECParameters parameters = GMNamedCurves.getByOID(GMObjectIdentifiers.sm2p256v1);
        ECParameterSpec parameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN());
        BigInteger privateKey = new BigInteger(keyBytes);
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(privateKey, parameterSpec);
        return new BCECPrivateKey(EC, privateKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    public static PublicKey publicKey(byte[] keyBytes) {
    	X9ECParameters parameters = GMNamedCurves.getByOID(GMObjectIdentifiers.sm2p256v1);
        ECParameterSpec parameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN());
        ECPoint ecPoint = parameters.getCurve().decodePoint(keyBytes);
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, parameterSpec);
        return new BCECPublicKey(EC, publicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt (byte[] data, BCECPublicKey publicKey) throws Exception {
    	Cipher cipher = Cipher.getInstance ("SM2", "BC");
        cipher.init (Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal (data);
    }

    public static byte[] encryptSm2 (byte[] data, BCECPublicKey publicKey) throws Exception {
    	SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;

        //通过公钥对象获取公钥的基本域参数。
        ECParameterSpec ecParameterSpec = publicKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //通过公钥值和公钥基本参数创建公钥参数对象
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
        //根据加密模式实例化SM2公钥加密引擎
        SM2Engine sm2Engine = new SM2Engine(mode);
        //初始化加密引擎
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;
        try {
            //将明文字符串转换为指定编码的字节串
            byte[] in = data;
            //通过加密引擎对字节数串行加密
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println("SM2加密时出现异常:" + e.getMessage());
            e.printStackTrace();
        }
        //将加密后的字节串转换为十六进制字符串
        return arrayOfBytes;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt (byte[] data, BCECPrivateKey privateKey) throws Exception {
    	 Cipher cipher = Cipher.getInstance ("SM2", "BC");
         cipher.init (Cipher.DECRYPT_MODE, privateKey);
         return cipher.doFinal (data);
    }

    public static byte[] decryptSm2 (byte[] data, BCECPrivateKey privateKey) throws Exception {
    	SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
       //通过私钥对象获取私钥的基本域参数。
       ECParameterSpec ecParameterSpec = privateKey.getParameters();
       ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
               ecParameterSpec.getG(), ecParameterSpec.getN());
       //通过私钥值和私钥钥基本参数创建私钥参数对象
       ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(),ecDomainParameters);
       //通过解密模式创建解密引擎并初始化
       SM2Engine sm2Engine = new SM2Engine(mode);
       sm2Engine.init(false, ecPrivateKeyParameters);
       byte[] arrayOfBytes= null;
       try {
           //通过解密引擎对密文字节串进行解密
           arrayOfBytes = sm2Engine.processBlock(data, 0, data.length);
           //将解密后的字节串转换为utf8字符编码的字符串（需要与明文加密时字符串转换成字节串所指定的字符编码保持一致）
       } catch (Exception e) {
           System.out.println("SM2解密时出现异常" + e.getMessage());
       }
       return arrayOfBytes;
   }

    /**
     * 私钥签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] sign (byte[] data, BCECPrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance (ALGORITHM, "BC");
        signature.initSign (privateKey);
        signature.update (data);
        return signature.sign ();
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param signature
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean verify (byte[] data, byte[] signature, BCECPublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance (ALGORITHM, "BC");
        verifier.initVerify (publicKey);
        verifier.update (data);
        return verifier.verify (signature);
    }

}
