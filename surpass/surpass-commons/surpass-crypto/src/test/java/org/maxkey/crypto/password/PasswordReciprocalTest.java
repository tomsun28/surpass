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






package org.maxkey.crypto.password;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.surpass.crypto.ReciprocalUtils;
import com.surpass.crypto.password.PasswordReciprocal;

public class PasswordReciprocalTest {

	public PasswordReciprocalTest() {

	}

	public static void main(String[] args) {
		String pass ="x8zPbCya";

		String encode = PasswordReciprocal.getInstance().encode(pass);
		System.out.println(encode);
		System.out.println(PasswordReciprocal.getInstance().decoder(encode));

		System.out.println(PasswordReciprocal.getInstance().matches(pass,encode));


		encode="$2a$10$hNuvgpizAqt7UPNhMWdF9uc136af921149577681171539320b39688d279c6f42e4d80fc4ea9fa5e4e89d8bb9229df89083abcd";
		System.out.println("PasswordReciprocal "+PasswordReciprocal.getInstance().decoder(encode));

	}

	@Test
	public void doBCrypt() {
		BCryptPasswordEncoder spe = new BCryptPasswordEncoder();
		//String pass=PasswordReciprocal.getInstance().rawPassword("admin", "admin");
		String pass = "x8zPbCya";
		String epass = spe.encode(pass);
		System.out.println("PasswordEncoder "+epass);
	}

	@Test
	public void doBShDES() {
		String salt = PasswordReciprocal.getInstance().gensalt();

		System.out.println("salt " + salt);

		String plain = "maxkey";
		String confuse = salt.substring(PasswordReciprocal.PREFFIX_LENGTH) + plain ;
		System.out.println("password " + confuse);

		String secretKey = salt.substring(PasswordReciprocal.PREFFIX_LENGTH);
		System.out.println("secretKey " + secretKey);
		String ciphertext  = salt + ReciprocalUtils.encode2Hex(confuse , salt.substring(PasswordReciprocal.PREFFIX_LENGTH));
		System.out.println("ciphertext " +ciphertext);

		System.out.println("plain " +PasswordReciprocal.getInstance().decoder(ciphertext));
	}

}
