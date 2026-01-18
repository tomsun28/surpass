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






package org.dromara;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Integer2LongTest {

    @Test
    public void test() {
        Integer intValue =20000;
        Long v=Integer.toUnsignedLong(intValue);
        System.out.println(v);
        System.out.println(v.getClass());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Integer intValue =20000;
        Long v=Integer.toUnsignedLong(intValue);
        System.out.println(v);
        System.out.println(v.getClass());


        //URLEncoder.encode("http%3A%2F%2Flocal.test.com%2F%23%2Foauth2%2Fvalidate","UTF-8");
        String d = URLDecoder.decode("http%3A%2F%2Flocal.test.com%2F%23%2Foauth2%2Fvalidate","UTF-8");
        d = URLDecoder.decode(d,"UTF-8");
        System.out.println(d);
    }

}
