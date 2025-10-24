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






package org.dromara.surpass.web;


import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import org.dromara.surpass.constants.ContentType;
import org.dromara.surpass.util.AuthorizationHeaderUtils;
import org.dromara.surpass.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * HttpRequestAdapter Http请求封装<br>
 *   支持Authorization header参数<br>
 * 	 支持XML,JSON,FORM
 */
@Component
public class HttpRequestAdapter {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestAdapter.class);

	static final String CONTENT_ENCODING = "UTF-8";
	protected String mediaType = ContentType.APPLICATION_FORM;

	protected Map<String,String> headers = new HashMap<>();

    public HttpRequestAdapter(){}

    public HttpRequestAdapter(String mediaType){
        this.mediaType = mediaType;
    }

	public String post(String url,Map<String, Object> parameterMap) {
		setContentType(ContentType.APPLICATION_FORM);
		return post(url , parameterMap , headers);
	}

	public HttpRequestAdapter addHeaderAuthorizationBearer(String token ) {
		headers.put("Authorization", AuthorizationHeaderUtils.createBearer(token));
		return this;
	}

	public HttpRequestAdapter addHeaderAuthorizationBasic(String username, String password) {
		headers.put("Authorization", AuthorizationHeaderUtils.createBasic(username,password));
		return this;
	}

	public HttpRequestAdapter setContentType(String contentType) {
		headers.put("Content-Type", contentType);
		return this;
	}

	public HttpRequestAdapter addHeader(String name , String value ) {
		headers.put(name, value);
		return this;
	}

    public String post(String url,Map<String, Object> parameterMap,Map<String,String> headers) {
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // 创建httpPost远程连接实例
        HttpPost httpMethod = new HttpPost(url);
        // 配置请求参数实例
        setRequestConfig(httpMethod);
        // 设置请求头
        buildHeader(httpMethod,headers);

        // 封装post请求参数
        if (null != parameterMap && parameterMap.size() > 0) {
            if(mediaType.equals(ContentType.APPLICATION_FORM)) {
                // 为httpPost设置封装好的请求参数
                httpMethod.setEntity(buildFormEntity(parameterMap));
            }else if(mediaType.equals(ContentType.APPLICATION_JSON)) {
                String jsonString = JsonUtils.toString(parameterMap);
                StringEntity stringEntity =
                		new StringEntity(jsonString, org.apache.hc.core5.http.ContentType.APPLICATION_JSON,CONTENT_ENCODING,false);
                httpMethod.setEntity(stringEntity);
            }
            logger.trace("Post Message \n{} ", httpMethod.getEntity());
        }

        try {
        	HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
            // httpClient对象执行post请求,并返回响应参数对象,从响应对象中获取响应内容
           return  httpClient.execute(httpMethod,responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(httpClient,httpResponse);// 关闭资源
        }
        return null;
    }


    public String post(String url,Object data) {
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // 创建httpPost远程连接实例
        HttpPost httpMethod = new HttpPost(url);
        // 配置请求参数实例
        setRequestConfig(httpMethod);
        // 设置请求头
        buildHeader(httpMethod,headers);

        // 封装put请求参数
        String jsonString = JsonUtils.toString(data);
        StringEntity stringEntity =
        		new StringEntity(jsonString, org.apache.hc.core5.http.ContentType.APPLICATION_JSON,CONTENT_ENCODING,false);
        httpMethod.setEntity(stringEntity);
        logger.trace("Post Message \n{} ", httpMethod.getEntity());
        try {
        	HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
            // httpClient对象执行put请求,并返回响应参数对象,从响应对象中获取响应内容
            return httpClient.execute(httpMethod,responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(httpClient,httpResponse);// 关闭资源
        }
        return null;
    }

    public String put(String url,Object data) {
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // 创建httpPost远程连接实例
        HttpPut httpMethod = new HttpPut(url);
        // 配置请求参数实例
        setRequestConfig(httpMethod);
        // 设置请求头
        buildHeader(httpMethod,headers);

        // 封装put请求参数
        String jsonString = JsonUtils.toString(data);
        StringEntity stringEntity =
        		new StringEntity(jsonString, org.apache.hc.core5.http.ContentType.APPLICATION_JSON,CONTENT_ENCODING,false);
        httpMethod.setEntity(stringEntity);
        logger.debug("Put Message \n{} ", httpMethod.getEntity());
        try {
        	HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
            // httpClient对象执行put请求,并返回响应参数对象,从响应对象中获取响应内容
            return httpClient.execute(httpMethod,responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(httpClient,httpResponse);// 关闭资源
        }
        return null;
    }


	public String get(String url) {
		headers.put("Content-Type", ContentType.APPLICATION_FORM);
		return get(url ,  headers);
	}

    public String get(String url,Map<String,String> headers) {
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // 创建httpPost远程连接实例
        HttpGet httpMethod = new HttpGet(url);
        // 配置请求参数实例
        setRequestConfig(httpMethod);
        // 设置请求头
        buildHeader(httpMethod,headers);
        logger.debug("get {} " , url);
        try {
        	HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
            // httpClient对象执行get请求,并返回响应参数对象,从响应对象中获取响应内容
            return httpClient.execute(httpMethod,responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(httpClient,httpResponse);// 关闭资源
        }
        return null;
    }

    public String delete(String url) {
    	 // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // 创建HttpDelete远程连接实例
        HttpDelete httpMethod = new HttpDelete(url);
        // 配置请求参数实例
        setRequestConfig(httpMethod);
        // 设置请求头
        buildHeader(httpMethod,headers);
        logger.debug("delete {} " , url);
        try {
        	HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
            // httpClient对象执行post请求,并返回响应参数对象,从响应对象中获取响应内容
            return httpClient.execute(httpMethod,responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(httpClient,httpResponse);// 关闭资源
        }
        return null;
	}

    /**
     * resolveHttpResponse get content from httpResponse
     * @param httpResponse
     * @return String
     * @throws ParseException
     * @throws IOException
     * @throws org.apache.hc.core5.http.ParseException
     */
    protected String resolveHttpResponse(CloseableHttpResponse httpResponse) throws IOException, ParseException {
   	 HttpEntity entity = httpResponse.getEntity();
        String content = EntityUtils.toString(entity);
        HttpStatus  httpStatus  = HttpStatus.valueOf(httpResponse.getCode());
        logger.debug("Http Response HttpStatus {} " , httpStatus);
        logger.trace("Http Response Content {} " , content );
        return content;
   }

    /**
     * @param HttpRequest
     * @param headers
     */
    protected void buildHeader(HttpUriRequestBase  httpRequest,Map<String,String> headers) {
    	// 设置请求头
        if (null != headers && headers.size() > 0) {
        	  Set<Entry<String, String>> entrySet = headers.entrySet();
              // 循环遍历，获取迭代器
              Iterator<Entry<String, String>> iterator = entrySet.iterator();
              while (iterator.hasNext()) {
                  Entry<String, String> mapEntry = iterator.next();
                  logger.trace("Name {} , Value {}",mapEntry.getKey(),mapEntry.getValue());
                  httpRequest.addHeader(mapEntry.getKey(), mapEntry.getValue());
              }
        }
    }

    protected UrlEncodedFormEntity buildFormEntity(Map<String, Object> parameterMap) {
    	 List<NameValuePair> nvps = new ArrayList<>();
         // 通过map集成entrySet方法获取entity
         Set<Entry<String, Object>> entrySet = parameterMap.entrySet();
         // 循环遍历，获取迭代器
         Iterator<Entry<String, Object>> iterator = entrySet.iterator();
         while (iterator.hasNext()) {
             Entry<String, Object> mapEntry = iterator.next();
             logger.trace("Name {} , Value {}",mapEntry.getKey(),mapEntry.getValue());
             nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
         }
         // 为httpPost设置封装好的请求参数
         return new UrlEncodedFormEntity(nvps);
    }

    protected void setRequestConfig(HttpUriRequestBase  httpMethod){
    	RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMicroseconds(35000))// 设置连接请求超时时间
                .build();
        // 为httpMethod实例设置配置
        httpMethod.setConfig(requestConfig);
    }

    /**
     * 关闭资源
     * @param httpClient
     * @param httpResponse
     */
    protected void close(CloseableHttpClient httpClient,CloseableHttpResponse httpResponse) {
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
