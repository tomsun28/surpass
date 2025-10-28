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






/**
 *
 */
package com.surpass.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.springframework.core.io.ClassPathResource;

import com.surpass.crypto.password.PasswordReciprocal;

/**
 * 类似linux或Unix上，md5sum是用来计算和校验文件报文摘要的工具程序
 * @author Crystal.Sea
 *
 */
public class Md5Sum {

	static String sumResource = "$2a$10$1NPu7JtdTO2TOBl6e.nnfu39dff446ffbd5f60115762b2450937711fcdc2d156724af4aff9c4181b21c1005c7a6e7b79be8315d6142c7186928f64ba27d01dacc9c74c0de8dcc536316322dc8655a7e8271df366d80044affcd665";

	static String passSum ="$2a$10$ASayvpWLQ0JrELpeNMJA9edff2f559c8f98b2755ad848ebc7c52b29ac0bc907e57664a09bbe1b9cfa0abaa8be011cba1627b6443f40cb47f4de5d61378994790273fd30ea8cc75f89391857eb1651a2cb66b90bffd55390e17c2a9";

	/**
	 *
	 */
	public Md5Sum() {

	}

	public static String produce(File file) {
		String md5value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(byteBuffer);
			byte[] bCipher=messageDigest.digest();
			md5value=HexUtils.bytes2HexString(bCipher);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		md5value += " *"+file.getName();
		return md5value;
	}

	public static String produce(InputStream is,String fileName) {
		String md5value = "";
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(BytesUtils.toByteArray(is));
			byte[] bCipher=messageDigest.digest();
			md5value=HexUtils.bytes2HexString(bCipher);
			md5value += " *"+fileName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return md5value;
	}

	public static boolean check(File file,String md5String) {
		String md5value	=	produce(file);

		return md5value.equals(md5String);
	}

	public static boolean check(InputStream is,String md5String) {
		String fileName = md5String.split("\\*")[1];
		String md5value	=	produce(is,fileName);

		return md5value.equals(md5String);
	}

	public static boolean checkVersion() {
		boolean checkResult = false;
		try {
			ClassPathResource classFile =
	                new ClassPathResource(
	                		PasswordReciprocal.getInstance().decoder(sumResource));
			checkResult = check(classFile.getInputStream(),PasswordReciprocal.getInstance().decoder(passSum));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if( !checkResult ) {
			System.exit(0);
		}

		return checkResult;
	}

}
