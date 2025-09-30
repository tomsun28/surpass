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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Exception.
 *
 * @author Crystal.Sea
 *
 */
@RestController
public class ExceptionEndpoint extends  AbstractErrorController    {
	private static  final Logger logger = LoggerFactory.getLogger(ExceptionEndpoint.class);

	static ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(
															ErrorAttributeOptions.Include.EXCEPTION,
															ErrorAttributeOptions.Include.STACK_TRACE,
															ErrorAttributeOptions.Include.MESSAGE,
															ErrorAttributeOptions.Include.BINDING_ERRORS
														);


	public ExceptionEndpoint(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}


    @GetMapping({ "/exception/error/400" })
    public ModelAndView error400(
            HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Exception BAD_REQUEST");
        return new ModelAndView("exception/400");
    }

    /**
     * //查看浏览器中的报错信息.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @GetMapping(value = { "/exception/error/404" })
    public ModelAndView error404(
            HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Exception PAGE NOT_FOUND ");
        return new ModelAndView("exception/404");
    }

    @GetMapping(value = { "/exception/error/500" })
    public ModelAndView error500(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Exception INTERNAL_SERVER_ERROR ");
        Map<String, Object> attributes = getErrorAttributes(request, errorAttributeOptions);
        logger.debug("Error attributes {} ",attributes);
        return new ModelAndView("exception/500",attributes);
    }

    @GetMapping(value = { "/exception/accessdeny" })
    public ModelAndView accessdeny(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("exception/accessdeny ");
        return new ModelAndView("exception/accessdeny");
    }
}
