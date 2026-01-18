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

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import static java.util.Objects.isNull;

public class SM4Utils {

    private static final int DEFAULT_KEY_SIZE = 128;
    private static final String ALGORITHM = "SM4";
    private static final String SM4_ECB_ = "SM4/ECB/";
    private static final String SM4_CBC_ = "SM4/CBC/";
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();

    static {
        if (isNull(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME))) {
            Security.addProvider(PROVIDER);
        }
    }

    public enum Padding {
        PKCS5("PKCS5Padding"),
        PKCS7("PKCS7Padding"),
        ISO10126("ISO10126Padding");

        private final String name;

        Padding(String name) {
            this.name = name;
        }
    }

    // region generateKey

    public static byte[] genKey()  throws Exception {
        return genKey(DEFAULT_KEY_SIZE);
    }


    public static byte[] genKey(int keySize)  throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    public static String genKeyAsHex()  throws Exception {
        return genKeyAsHex(DEFAULT_KEY_SIZE);
    }

    public static String genKeyAsHex(int keySize)  throws Exception {
        return Hex.toHexString(genKey(keySize));
    }

    public static String genKeyAsBase64()  throws Exception {
        return genKeyAsBase64(DEFAULT_KEY_SIZE);
    }

    public static String genKeyAsBase64(int keySize)  throws Exception {
        return BASE64_ENCODER.encodeToString(genKey(keySize));
    }

    // endregion generateKey

    // region ECB mode

    public static Cipher getCipher_ECB(Padding padding)  throws Exception {
        return Cipher.getInstance(SM4_ECB_ + padding.name, BouncyCastleProvider.PROVIDER_NAME);
    }

    /**
     * 使用指定的加密算法和密钥对给定的字节数组进行加密
     *
     * @param data 要加密的字节数组
     * @param key  加密所需的密钥
     * @return byte[]   加密后的字节数组
     */
    public static byte[] encrypt_ECB(byte[] data, byte[] key, Padding padding)  throws Exception {
        Cipher cipher = getCipher_ECB(padding);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }

    /**
     * 使用指定的加密算法和密钥对给定的字节数组进行解密
     *
     * @param data 要解密的字节数组
     * @param key  解密所需的密钥
     * @return byte[]   解密后的字节数组
     */
    public static byte[] decrypt_ECB(byte[] data, byte[] key, Padding padding) throws Exception {
        Cipher cipher = getCipher_ECB(padding);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }

    public static String encryptHex_ECB(String data, String key, Padding padding) throws Exception {
        return Hex.toHexString(encrypt_ECB(
                data.getBytes(StandardCharsets.UTF_8),
                Hex.decode(key),
                padding
        ));
    }

    public static String decryptHex_ECB(String data, String key, Padding padding)  throws Exception {
        return new String(decrypt_ECB(
                Hex.decode(data),
                Hex.decode(key),
                padding
        ), StandardCharsets.UTF_8);
    }

    public static String encryptBase64_ECB(String data, String key, Padding padding)  throws Exception {
        return BASE64_ENCODER.encodeToString(encrypt_ECB(
                data.getBytes(StandardCharsets.UTF_8),
                BASE64_DECODER.decode(key),
                padding
        ));
    }

    public static String decryptBase64_ECB(String data, String key, Padding padding) throws Exception {
        return new String(decrypt_ECB(
                BASE64_DECODER.decode(data),
                BASE64_DECODER.decode(key),
                padding
        ), StandardCharsets.UTF_8);
    }

    // endregion ECB mode

    // region CBC mode

    public static Cipher getCipher_CBC(Padding padding) throws Exception {
        return Cipher.getInstance(SM4_CBC_ + padding.name, BouncyCastleProvider.PROVIDER_NAME);
    }

    /**
     * 使用指定的加密算法和密钥对给定的字节数组进行加密
     *
     * @param data 要加密的字节数组
     * @param key  加密所需的密钥
     * @param iv   解密所需的 IV
     * @return byte[]   加密后的字节数组
     * @throws NoSuchPaddingException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encrypt_CBC(byte[] data, byte[] key, byte[] iv, Padding padding)  throws Exception  {
        Cipher cipher = getCipher_CBC(padding);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(data);
    }

    /**
     * 使用指定的加密算法和密钥对给定的字节数组进行解密
     *
     * @param data 要解密的字节数组
     * @param key  解密所需的密钥
     * @param iv   解密所需的 IV
     * @return byte[]   解密后的字节数组
     * @throws Exception
     * @throws InvalidKeyException
     */
    public static byte[] decrypt_CBC(byte[] data, byte[] key, byte[] iv, Padding padding) throws Exception {
        Cipher cipher = getCipher_CBC(padding);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    public static String encryptHex_CBC(String data, String key, String iv, Padding padding) throws Exception {
        return Hex.toHexString(encrypt_CBC(
                data.getBytes(StandardCharsets.UTF_8),
                Hex.decode(key),
                Hex.decode(iv),
                padding
        ));
    }

    public static String decryptHex_CBC(String data, String key, String iv, Padding padding) throws Exception {
        return new String(decrypt_CBC(
                Hex.decode(data),
                Hex.decode(key),
                Hex.decode(iv),
                padding
        ), StandardCharsets.UTF_8);
    }

    public static String encryptBase64_CBC(String data, String key, String iv, Padding padding) throws Exception{
        return BASE64_ENCODER.encodeToString(encrypt_CBC(
                data.getBytes(StandardCharsets.UTF_8),
                BASE64_DECODER.decode(key),
                BASE64_DECODER.decode(iv),
                padding
        ));
    }

    public static String decryptBase64_CBC(String data, String key, String iv, Padding padding) throws  Exception {
        return new String(decrypt_CBC(
                BASE64_DECODER.decode(data),
                BASE64_DECODER.decode(key),
                BASE64_DECODER.decode(iv),
                padding
        ), StandardCharsets.UTF_8);
    }

    // endregion CBC mode
}
