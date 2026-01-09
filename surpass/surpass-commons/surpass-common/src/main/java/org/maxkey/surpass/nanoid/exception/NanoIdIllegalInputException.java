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



package org.maxkey.surpass.nanoid.exception;

/**
 * An exception that should be thrown if an illegal parameter has been used
 */
public class NanoIdIllegalInputException extends RuntimeException {

    /**
	 *
	 */
	private static final long serialVersionUID = -7370036433261133768L;

	/**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NanoIdIllegalInputException(String message) {
        super(message);
    }
}
