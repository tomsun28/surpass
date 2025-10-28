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






package com.surpass.crypto.password;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.surpass.crypto.*;

public class SM3PasswordEncoder  implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return new String(Hex.encode(SM3Util.encode(rawPassword.toString().getBytes())));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String  cipher = encode(rawPassword);
        return encodedPassword.equals(cipher);
    }

}
