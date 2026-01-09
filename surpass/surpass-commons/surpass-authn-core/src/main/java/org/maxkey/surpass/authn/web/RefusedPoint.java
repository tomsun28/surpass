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






package org.maxkey.surpass.authn.web;

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
 * 	"status" :403 ,
 *  "error" :"Refused"
 *  "message": "Refused",
 *  "path" : "/"
 * }
 * </p>
 *
 * @author Crystal.Sea
 *
 */
@Controller
@RequestMapping(value = "/auth")
public class RefusedPoint {
	private static final Logger logger = LoggerFactory.getLogger(RefusedPoint.class);

 	@GetMapping(value={"/refusedpoint"})
	public void refusedPoint(HttpServletRequest request, HttpServletResponse response) throws IOException {
 		logger.trace("RefusedPoint /refusedpoint.");
 		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
 	    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

 	    final Map<String, Object> responseBody = new HashMap<>();
 	    responseBody.put("status", HttpServletResponse.SC_FORBIDDEN);
 	    responseBody.put("error", "Refused");
 	    responseBody.put("message", "Refused");
 	    responseBody.put("path", request.getServletPath());

 	    final ObjectMapper mapper = new ObjectMapper();
 	    mapper.writeValue(response.getOutputStream(), responseBody);
 	}
}
