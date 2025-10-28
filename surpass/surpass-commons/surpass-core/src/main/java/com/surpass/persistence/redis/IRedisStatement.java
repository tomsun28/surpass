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






package com.surpass.persistence.redis;

import java.io.Serializable;
import java.util.List;

public interface IRedisStatement {

	public  void set(String key, String value);

	public  void setObject(String key, Object value);

	public  void setexObject(String key,int seconds, Object value);

	public  void setex(String key,long seconds, String value);

	public  String get(String key);

	public  <T> T getObject(String key);

	public void expire(String key,long seconds);

	public void del(String key);

	public  void rPush(String key, Serializable object);

	public long  lRem(String key,int count,String value);

	public List<String>  lRange(String key,int start,int end);

	public  void close();
}
