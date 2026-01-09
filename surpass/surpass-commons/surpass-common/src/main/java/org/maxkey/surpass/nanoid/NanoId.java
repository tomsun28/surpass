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



package org.maxkey.surpass.nanoid;

import java.security.SecureRandom;
import java.util.function.Function;

import org.maxkey.surpass.nanoid.exception.NanoIdIllegalInputException;
import org.maxkey.surpass.nanoid.exception.RandomGeneratorMissingException;

/**
 * Class capable to generate {@link NanoId}
 */
public class NanoId {
    private static final SecureRandom RANDOM_GENERATOR = new SecureRandom();
    private static final Alphabet URL_ALPHABET = Alphabet.urlSafe();
    private static final Function<Integer, byte[]> DEFAULT_RANDOM_GENERATOR = byteNumber -> {
        var randomBytes = new byte[byteNumber];
        RANDOM_GENERATOR.nextBytes(randomBytes);
        return randomBytes;
    };

    /**
     * Generates nanoid with defaults
     *
     * @return {@link String} representation of a {@link NanoId}
     */
    public static String randomNanoId() {
        return randomNanoId(21);
    }

    /**
     * Generates nanoid
     *
     * @param size length of the nanoid to return
     * @return {@link String} representation of a {@link NanoId}
     */
    public static String randomNanoId(int size) {
        return randomNanoId(size, URL_ALPHABET);
    }

    /**
     * Generates nanoid
     *
     * @param size     length of the nanoid to return
     * @param alphabet an alphabet to choose the letters from
     * @return {@link String} representation of a {@link NanoId}
     */
    public static String randomNanoId(int size, Alphabet alphabet) {
        return randomNanoId(size, alphabet, DEFAULT_RANDOM_GENERATOR);
    }

    /**
     * Generates nanoid
     *
     * @param size                 length of the nanoid to return
     * @param alphabet             an alphabet to choose the letters from
     * @param randomBytesGenerator specifies a generator to generate random bytes
     * @return {@link String} representation of a {@link NanoId}
     * @throws NanoIdIllegalInputException     when size is lower than 0 or alphabet is not provided
     * @throws RandomGeneratorMissingException when random generator has not been provided.
     */
    public static String randomNanoId(int size, Alphabet alphabet, Function<Integer, byte[]> randomBytesGenerator) {

        if (randomBytesGenerator == null) {
            throw new RandomGeneratorMissingException();
        }

        if (alphabet == null) {
            throw new NanoIdIllegalInputException("alphabet not provided");
        }

        if (size < 0) {
            throw new NanoIdIllegalInputException("size must be greater than 0");
        }

        var randomBytes = randomBytesGenerator.apply(size);

        var builder = new StringBuilder(size);

        for (byte randomByte : randomBytes) {
            var aChar = alphabet.resolveChar(randomByte);
            builder.append(aChar);
        }

        return builder.toString();
    }

}
