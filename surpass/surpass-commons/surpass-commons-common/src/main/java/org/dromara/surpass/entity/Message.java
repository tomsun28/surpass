/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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




package org.dromara.surpass.entity;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import lombok.Data;

/**
 * 返回信息内容<br>
 * code 返回码 <br>
 * message 消息提示 <br>
 * data 返回数据内容 <br>
 *
 * @param <T>
 */
@Data
public class Message<T> {

    public static final int SUCCESS = 0;    //成功
    public static final int ERROR = 1;    //错误
    public static final int FAIL = 2;    //失败
    public static final int INFO = 101;    //信息
    public static final int PROMPT = 102;    //提示
    public static final int WARNING = 103;    //警告

    int code;

    String message;

    Date timestamp;

    T data;

    public Message() {
        this.code = SUCCESS;
        this.timestamp = new Date();
    }

    public Message(int code) {
        this.code = code;
        this.timestamp = new Date();
    }

    public Message(T data) {
        this.data = data;
        this.timestamp = new Date();
    }

    public Message(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = new Date();
    }

    public Message(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = new Date();
    }

    public Message(int code, T data) {
        this.code = code;
        this.data = data;
        this.timestamp = new Date();
    }

    public static <T> Message<T> ok(T data) {
        return new Message<>(SUCCESS, data);
    }

    public static <T> Message<T> failed(String message) {
        return new Message<>(FAIL, message);
    }

    public void setMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setData(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseEntity<?> buildResponse() {
        return ResponseEntity.ok(this);
    }

}
