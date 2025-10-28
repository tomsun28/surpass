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



package com.surpass.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.surpass.entity.Institutions;
import com.surpass.util.DateUtils;
import com.surpass.util.IdGenerator;

/**
 * Application is common class for Web Application Context.
 *
 * @author Crystal.Sea
 * @since 1.5
 */
/**
 * @author shimi
 *
 */
public final class WebContext {
	static final Logger logger = LoggerFactory.getLogger(WebContext.class);

    static StandardEnvironment properties;

    static ApplicationContext context;

    static IdGenerator idGenerator = new IdGenerator();

    public static final String 	UNKNOWN_IPADDRESS = "unknown";

    /**
     * clear session Message ,session id is Constants.MESSAGE
     *
     * @see WebConstants.MESSAGE
     */
    public static void clearMessage() {
        removeAttribute(WebConstants.CURRENT_MESSAGE);
    }

    public static void init(ApplicationContext applicationContext){
    	context = applicationContext;
		if (context.containsBean("propertySourcesPlaceholderConfigurer")) {
			logger.trace("init Context properties");
            PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
                    ((PropertySourcesPlaceholderConfigurer) applicationContext
                    .getBean("propertySourcesPlaceholderConfigurer"));
            PropertySource<?>  propertySource =
            		propertySourcesPlaceholderConfigurer
            			.getAppliedPropertySources()
            			.get(PropertySourcesPlaceholderConfigurer.ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME);
            if(propertySource != null) {
            	properties =  (StandardEnvironment) propertySource.getSource();
            }
		}
    }

    /**
     * get ApplicationContext from web  ServletContext configuration
     * @return ApplicationContext
     */
    public static ApplicationContext getContext(){
        return context;
    }

    public static StandardEnvironment getProperties() {
		return properties;
	}

    public static String getProperty(String property) {
    	return properties.getProperty(property);
    }

    public static String getServerPort(){
    	return properties.getProperty("server.port");
    }

	/**
     * get bean from spring configuration by bean id
     * @param id
     * @return Object
     */
    public static Object getBean(String name){
        if(getContext() == null) {
            return getContext().getBean(name);
        }else {
            return context.getBean(name);
        }
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException{
    	if(getContext() == null) {
            return getContext().getBean(name,requiredType);
        }else {
            return context.getBean(name,requiredType);
        }
    }

    // below method is common HttpServlet method
    /**
     * get Spring HttpServletRequest.
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
    	return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }


    /**
     * get Http Context full Path.
     *
     * @return String HttpContextPath
     */
    public static String getContextPath(boolean isContextPath) {
        return getContextPath(WebContext.getRequest(),isContextPath);
    }

    /**
     * get Http Context full Path,if port equals 80 or 443 is omitted.
     *
     * @return String eg:http://192.168.1.20:9080/webcontext or
     *         http://www.website.com/webcontext
     */
    public static String getContextPath(HttpServletRequest request,boolean isContextPath) {
    	String fullRequestUrl = UrlUtils.buildFullRequestUrl(request);
        StringBuilder url = new StringBuilder(fullRequestUrl.substring(0, fullRequestUrl.indexOf(request.getContextPath())));
        if(isContextPath) {
        	url.append(request.getContextPath());
        }
        logger.trace("http ContextPath {}" , url);
        return url.toString();

    }

    /**
     * isTraceEnabled print request headers and parameters<br>
     * see WebInstRequestFilter
     * @param request
     */
    public static void printRequest(final HttpServletRequest request) {
    	String url = request.getRequestURI().substring(request.getContextPath().length());
		logger.trace("getContextPath : {}"  , request.getContextPath());
    	logger.trace("getRequestURL : {} " , request.getRequestURL());
		logger.trace("URL : {}" , url);
    	logger.trace("getMethod : {} " , request.getMethod());

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
          String key = headerNames.nextElement();
          String value = request.getHeader(key);
          logger.debug("Header key {} , value {}" , key, value);
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
          String key = parameterNames.nextElement();
          String value = request.getParameter(key);
          logger.info("Parameter {} , value {}",key , value);
        }
    }

    /**
     * get current Session.
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * get current Session,if no session ,new Session created.
     *
     * @return HttpSession
     */
    public static HttpSession getSession(boolean create) {
    	logger.debug("new Session created");
        return getRequest().getSession(create)  ;
    }

    /**
     * set Attribute to session ,Attribute name is name,value is value.
     *
     * @param name String
     * @param value String
     */
    public static void setAttribute(String name, Object value) {
    	getRequest().getSession().setAttribute(name, value);
    }

    /**
     * get Attribute from session by name.
     *
     * @param name String
     * @return
     */
    public static Object getAttribute(String name) {
        return getRequest().getSession().getAttribute(name) ;
    }

    /**
     * remove Attribute from session by name.
     *
     * @param name String
     */
    public static void removeAttribute(String name) {
    	getRequest().getSession().removeAttribute(name);
    }

    /**
     * get Request Parameter by name.
     *
     * @param name String
     * @return String
     */
    public static String getParameter(String name) {
        return  getRequest().getParameter(name);
    }

    public static Institutions getInst() {
        return (Institutions)getAttribute(WebConstants.CURRENT_INST);
    }

    /**
     * get locale from Spring Resolver,if locale is null,get locale from Spring.
     * SessionLocaleResolver this is from internationalization
     *
     * @return Locale
     */
    public static Locale getLocale() {
    	Locale locale = null;
        try {
            CookieLocaleResolver cookieLocaleResolver = getBean("localeResolver",CookieLocaleResolver.class);
            locale = cookieLocaleResolver.resolveLocale(getRequest());
        } catch (Exception e) {
        	logger.debug("getLocale() error . ");
            locale = RequestContextUtils.getLocale(getRequest());
        }
        return locale;
    }

    public static Map<String, String> getRequestParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String,String[]> entry : parameters.entrySet()) {
            map.put(entry.getKey(), entry.getValue() != null && entry.getValue().length > 0 ? entry.getValue()[0] : null);
        }
        return map;
    }

    /**
     * 根据名字获取cookie.
     *
     * @param request HttpServletRequest
     * @param name  cookie名字
     * @return Cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = getCookieAll(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面.
     *
     * @param request HttpServletRequest
     * @return Map
     */
    private static Map<String, Cookie> getCookieAll(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 保存Cookies.
     *
     * @param response response响应
     * @param name cookie的名字
     * @param value cookie的值
     * @param time cookie的存在时间
     */
    public static HttpServletResponse setCookie(
            HttpServletResponse response, String domain ,String name, String value, int time) {
        // new一个Cookie对象,键值对为参数
        Cookie cookie = new Cookie(name, value);
        // tomcat下多应用共享
        cookie.setPath("/");
        if(domain != null) {
            cookie.setDomain(domain);
        }
        // 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码
        try {
            URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 单位：秒
        if(time >= 0) {
            cookie.setMaxAge(time);
        }
        // 将Cookie添加到Response中,使之生效
        response.addCookie(cookie); // addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie
        return response;
    }

    public static HttpServletResponse expiryCookie(
            HttpServletResponse response, String domain ,String name, String value) {
        WebContext.setCookie(response,domain,name, value,0);
        return response;
    }

    public static HttpServletResponse setCookie(
            HttpServletResponse response, String domain ,String name, String value) {
        WebContext.setCookie(response,domain,name, value,-1);
        return response;
    }

    /**
     * get Current Date,eg 2012-07-10.
     *
     * @return String
     */
    public static String getCurrentDate() {
        return DateUtils.getCurrentDateAsString(DateUtils.FORMAT_DATE_YYYY_MM_DD);
    }

    /**
     * get Request IpAddress,for current Request.
     *
     * @return String,100.167.216.100
     */
    public static final String getRequestIpAddress() {
        return getRequestIpAddress(getRequest()) ;
    }

    /**
     * get Request IpAddress by request.
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static final String getRequestIpAddress(HttpServletRequest request) {
        String requestIpAddress = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(requestIpAddress) || UNKNOWN_IPADDRESS.equalsIgnoreCase(requestIpAddress)) {
        	requestIpAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(requestIpAddress) || UNKNOWN_IPADDRESS.equalsIgnoreCase(requestIpAddress)) {
        	requestIpAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(requestIpAddress) || UNKNOWN_IPADDRESS.equalsIgnoreCase(requestIpAddress)) {
        	requestIpAddress = request.getRemoteAddr();
        }
        if(requestIpAddress.indexOf(",")>-1) {
        	String[] ipAddress = requestIpAddress.split(",");
        	requestIpAddress = ipAddress[0];
        }
        logger.trace("RequestIpAddress: {}" , requestIpAddress);
        return requestIpAddress;
    }

    /**
     * captchaValid.
     * @param captcha String
     * @return
     */
    public static boolean captchaValid(String captcha) {
    	String sessionKey =  (String)WebContext.getSession().getAttribute(WebConstants.KAPTCHA_SESSION_KEY);
        return StringUtils.isNotBlank(sessionKey) && StringUtils.isNotBlank(captcha)  && captcha.equals(sessionKey);
    }

    /**
     * getI18nValue.
     *  @param code String
     * @return
     */
    public static String getI18nValue(String code) {
        String message = code;
        try {
            message = getContext().getMessage(
                code,
                null,
                getLocale());
        } catch (Exception e) {
        	logger.error("Exception" , e);
        }
        return message;
    }

    /**
     * getI18nValue.
     * @param code String
     * @param filedValues Object
     * @return
     */
    public static String getI18nValue(String code, Object[] filedValues) {
        String message = code;
        try {
            message = getContext().getMessage(
                code,
                filedValues,
                getLocale());
        } catch (Exception e) {
        	logger.error("Exception" , e);
        }
        return message;
    }

    /**
     * generate random Universally Unique Identifier,delete -.
     *
     * @return String
     */
    public static String genId() {
        return idGenerator.generate();
    }

    public static void setIdGenerator(IdGenerator idGenerator) {
		WebContext.idGenerator = idGenerator;
	}

	public static ModelAndView redirect(String redirectUrl) {
        return new ModelAndView("redirect:" + redirectUrl);
    }

    public static ModelAndView forward(String forwardUrl) {
        return new ModelAndView("forward:" + forwardUrl);
    }

    public static String version() {
		StringBuffer version = new StringBuffer("");
		version.append("-----------------------------------------------------------");
		version.append("+                      Surpass Enterprise Edition  ");
		version.append("+                      Accounting   Software        ");
		version.append("+                           Version %s".formatted(
                        WebContext.getProperty("application.formatted-version")));
		version.append("+");
		version.append("+                 %sCopyright 2024 - %s https://www.Surpass.com/".formatted(
        			    (char)0xA9 , new DateTime().getYear()));
		version.append("+                 .         All rights reserved         . ");
		version.append("-----------------------------------------------------------");
		return version.toString();
    }
}
