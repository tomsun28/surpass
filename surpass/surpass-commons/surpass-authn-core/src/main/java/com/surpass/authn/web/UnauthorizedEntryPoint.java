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






package com.surpass.authn.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 未认证接口返回/auth/entrypoint
 * <p>
 * {
 * 	"status" :401 ,
 *  "error" :"Unauthorized"
 *  "message": "Unauthorized",
 *  "path" : "/"
 * }
 * </p>
 *
 * @author Crystal.Sea
 *
 */
@Controller
@RequestMapping(value = "/auth")
public class UnauthorizedEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);

 	@GetMapping(value={"/entrypoint"})
	public void entryPoint(HttpServletRequest request, HttpServletResponse response) throws IOException {
 		logger.trace("UnauthorizedEntryPoint /entrypoint.");
 		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
 	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

 	    final Map<String, Object> responseBody = new HashMap<>();
 	    responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
 	    responseBody.put("error", "Unauthorized");
 	    responseBody.put("message", "Unauthorized");
 	    responseBody.put("path", request.getServletPath());

 	    final ObjectMapper mapper = new ObjectMapper();
 	    mapper.writeValue(response.getOutputStream(), responseBody);
 	}

}
