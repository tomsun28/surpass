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






package org.maxkey.surpass.persistence.redis.statement;

import java.io.Serializable;
import java.util.List;

import org.maxkey.surpass.persistence.redis.IRedisStatement;
import org.maxkey.surpass.persistence.redis.RedisConfigConsts;
import org.maxkey.surpass.util.ObjectTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ClusterPipeline;
import redis.clients.jedis.JedisCluster;

public class RedisClusterStatement  implements IRedisStatement{
	private static final Logger logger = LoggerFactory.getLogger(RedisClusterStatement.class);

	JedisCluster cluster ;

	ClusterPipeline pipeline ;

	public RedisClusterStatement() {

	}

	public RedisClusterStatement(JedisCluster cluster) {
		this.cluster = cluster;
	}

	/**
	 * @param key
	 * @param value
	 */
	public  void set(String key, String value){
		cluster.set(key, value);
	}


	/**
	 * @param key
	 * @param value
	 */
	public  void setObject(String key, Object value){
		if(value instanceof Serializable serialValue) {
			set(key, ObjectTransformer.serialize(serialValue));
		}else {
			logger.error("value must implements of Serializable .");
		}
	}

	public  void setexObject(String key,int seconds, Object value){
		if(value instanceof Serializable  serialValue) {
			setex(key, seconds, ObjectTransformer.serialize(serialValue));
		}else {
			logger.error("value must implements of Serializable .");
		}
	}

	/**
	 * @param key
	 * @param seconds
	 * @param value
	 */
	public  void setex(String key,long seconds, String value){
		if(seconds == 0){
			cluster.setex(key, RedisConfigConsts.DEFAULT_LIFETIME, value);
		}else{
			cluster.setex(key, seconds, value);
		}
	}


	/**
	 * @param key
	 * @return String
	 */
	public  String get(String key){
		String value = null;
		if(key != null){
			value = cluster.get(key);
		}
		return value;
	}

	/**
	 * @param key
	 * @return String
	 */
	public  <T> T getObject(String key){
		String value = null;
		if(key != null){
			value = get(key);
			if(value!=null){
				return ObjectTransformer.deserialize(value);
			}
		}
		return null;
	}

	public void expire(String key,long seconds){
		cluster.expire(key, seconds);
	}

	public void del(String key){
		cluster.del(key);
	}

	public  void rPush(String key, Serializable object){
		cluster.rpush(key, ObjectTransformer.serialize(object));
	}
	public long  lRem(String key,int count,String value){
		return cluster.lrem(key, count, value);
	}


	public List<String>  lRange(String key,int start,int end){
		return cluster.lrange(key, start, end);
	}

	public void openPipeline(){
		//this.pipeline = cluster.pipelined();
	}

	public List<Object> closePipeline(){
		 pipeline.sync();
		 //pipeline.
		 return null;
	}
	/**
     * jedis自动资源释放
     * @param jedis
     */
	public  void close() {
       // if (cluster != null) {
       //	cluster.close();
       // }
    }

	public ClusterPipeline getPipeline() {
		return pipeline;
	}


}
