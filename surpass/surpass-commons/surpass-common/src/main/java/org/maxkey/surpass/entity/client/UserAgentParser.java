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




package org.maxkey.surpass.entity.client;

import org.apache.commons.lang3.StringUtils;
import org.maxkey.surpass.crypto.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class UserAgentParser {
	static final Logger logger = LoggerFactory.getLogger(UserAgentParser.class);

    public static ClientUserAgent  resolveUserAgent(HttpServletRequest  request) {
    	ClientUserAgent browserUserAgent = new ClientUserAgent();
        String userAgent = getUserAgent(request);
        if(StringUtils.isNotBlank(userAgent)) {
        	hash(browserUserAgent,userAgent);
	        if (userAgent.indexOf(ConstBrowserType.MSIE.getName()) > -1) {
	        	msie(browserUserAgent,userAgent);
	        } else if (userAgent.indexOf(ConstBrowserType.EDG.getName()) > -1) {
	        	edg(browserUserAgent,userAgent);
	        }else if (userAgent.indexOf(ConstBrowserType.TRIDENT.getName()) > -1) {
	        	trident(browserUserAgent,userAgent);
	        } else if (userAgent.indexOf(ConstBrowserType.CHROME.getName()) > -1) {
	        	chrome(browserUserAgent,userAgent);
	        } else if (userAgent.indexOf(ConstBrowserType.FIREFOX.getName()) > -1) {
	        	firefox(browserUserAgent,userAgent);
	        }else if (userAgent.indexOf(ConstBrowserType.CLIENTAPP.getName()) > -1) {
	        	clientAPP(browserUserAgent,userAgent);
	        }else if (userAgent.indexOf(ConstBrowserType.SAFARI.getName()) > -1) {
	        	safari(browserUserAgent,userAgent);
	        }else {
	        	browserUserAgent.setPlatform(userAgent);
	        }
        }
        logger.debug("ClientUserAgent  {}" , browserUserAgent);
        return browserUserAgent;
    }

    static void msie(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent =  userAgent.split(";");
    	browserUserAgent.setName(arrayUserAgent[1].trim());
    	browserUserAgent.setPlatform(arrayUserAgent[2].trim());
    }

    static void edg(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent = userAgent.split(" ");
    	for (int i = 0; i < arrayUserAgent.length; i++) {
            if (arrayUserAgent[i].contains(ConstBrowserType.EDG.getName())) {
            	browserUserAgent.setName( arrayUserAgent[i].trim());
            	browserUserAgent.setName(browserUserAgent.getName().substring(0, browserUserAgent.getName().indexOf('.')));
            }
        }
    	 browserUserAgent.setPlatform( (arrayUserAgent[1].substring(1) + " " + arrayUserAgent[2] + " "
                 + arrayUserAgent[3].substring(0, arrayUserAgent[3].length() - 1)).trim());
    }

    static void trident(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent = userAgent.split(";");
         browserUserAgent.setName( ConstBrowserType.TRIDENT.getBrowser()+"/" + arrayUserAgent[3].split("\\)")[0]);
         browserUserAgent.setPlatform( arrayUserAgent[0].split("\\(")[1]);
    }

    static void chrome(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent = userAgent.split(" ");
         for (int i = 0; i < arrayUserAgent.length; i++) {
             if (arrayUserAgent[i].contains(ConstBrowserType.CHROME.getName())) {
             	browserUserAgent.setName( arrayUserAgent[i].trim());
             	browserUserAgent.setName( browserUserAgent.getName().substring(0, browserUserAgent.getName().indexOf('.')));
             }
         }
         browserUserAgent.setPlatform( (arrayUserAgent[1].substring(1) + " " + arrayUserAgent[2] + " "
                 + arrayUserAgent[3].substring(0, arrayUserAgent[3].length() - 1)).trim());
    }

    static void firefox(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent = userAgent.split(" ");
    	for (int i = 0; i < arrayUserAgent.length; i++) {
		     if (arrayUserAgent[i].contains(ConstBrowserType.FIREFOX.getName())) {
		     	browserUserAgent.setName( arrayUserAgent[i].trim());
		     	browserUserAgent.setName(browserUserAgent.getName().substring(0, browserUserAgent.getName().indexOf('.')));
		     }
    	}
    	browserUserAgent.setPlatform( (arrayUserAgent[1].substring(1) + " " + arrayUserAgent[2] + " "
		     + arrayUserAgent[3].substring(0, arrayUserAgent[3].length() - 1)).trim());
    }

    static void safari(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent = userAgent.split(" ");
    	browserUserAgent.setName(arrayUserAgent[arrayUserAgent.length-1]);
    	browserUserAgent.setPlatform((arrayUserAgent[3] + " " + arrayUserAgent[4] +" "+ arrayUserAgent[5]).trim());
    }

    static void clientAPP(ClientUserAgent browserUserAgent,String userAgent) {
    	String[] arrayUserAgent = userAgent.split(" ");
        browserUserAgent.setName(arrayUserAgent[arrayUserAgent.length-1]);
        browserUserAgent.setPlatform(arrayUserAgent[1].substring(1) + " " + arrayUserAgent[2].substring(0, arrayUserAgent[2].length() - 1).trim());
    }

    static String getUserAgent(HttpServletRequest  request){
        return (request != null ? request.getHeader("User-Agent") : null);
    }

    static void hash(ClientUserAgent browserUserAgent,String userAgent) {
    	browserUserAgent.setUserAgentHash(DigestUtils.md5Hex(userAgent));
    }

}
