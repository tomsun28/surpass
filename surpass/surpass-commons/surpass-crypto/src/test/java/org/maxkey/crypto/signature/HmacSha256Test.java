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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import org.apache.commons.codec.binary.Base64;
import org.maxkey.surpass.crypto.HexUtils;
import org.maxkey.surpass.crypto.HmacSha256;

public class HmacSha256Test {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
		String key ="7heM-14BtxjyKPuH3ITIm7q2-ps5MuBirWCsrrdbzzSAOuSPrbQYiaJ54AeA0uH2XdkYy3hHAkTFIsieGkyqxOJZ_dQzrCbaYISH9rhUZAKYx8tUY0wkE4ArOC6LqHDJarR6UIcMsARakK9U4dhoOPO1cj74XytemI-w6ACYfzRUn_Rn4e-CQMcnD1C56oNEukwalf06xVgXl41h6K8IBEzLVod58y_VfvFn-NGWpNG0fy_Qxng6dg8Dgva2DobvzMN2eejHGLGB-x809MvC4zbG7CKNVlcrzMYDt2Gt2sOVDrt2l9YqJNfgaLFjrOEVw5cuXemGkX1MvHj6TAsbLg";
		String msg="hkkkk";
		String encodeHex = HmacSha256.encodeHex(msg, key);
		System.out.println(encodeHex);
		System.out.println(Base64.encodeBase64String(HexUtils.hex2Bytes(encodeHex)));
		String hex="fb52123cd6bd5d6b4e9f0e5574eec7dd4fa9a4bfeb8a359b2ff5248650752e57";
		System.out.println(HmacSha256.verifyHex(msg, key , hex));

		String username = "maxkey";
		String code  = ""+Instant.now().getEpochSecond();

		String hmacSha256String = HmacSha256.encodeHex(username+key+code,key);
		System.out.println(hmacSha256String);
	}

}
