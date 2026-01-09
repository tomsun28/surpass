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






package org.maxkey.surpass.authn.jwt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.maxkey.surpass.authn.SignedPrincipal;
import org.maxkey.surpass.constants.ConstsJwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端认证JWT
 *
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
public class AuthJwt implements Serializable {

	private static final long serialVersionUID = -914373258878811144L;

	private String ticket;

	private String type = "Bearer";

	private String token;

	@JsonProperty(ConstsJwt.REFRESH_TOKEN)
	private String refreshToken;

	@JsonProperty(ConstsJwt.EXPIRES_IN)
	private int expiresIn;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private int   twoFactor;

	private String remeberMe;
	private String id;
	private String name;
	private String username;
	private String displayName;
	private String email;
	private String mobile;
	private String instId;
	private String instName;
	private int    passwordSetType;
	private List<String> authorities;

	public AuthJwt(String ticket, String type, String token, String refreshToken, int expiresIn, String remeberMe,
			String id, String name, String username, String displayName, String email, String instId, String instName,
			int passwordSetType, List<String> authorities) {
		super();
		this.ticket = ticket;
		this.type = type;
		this.token = token;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
		this.remeberMe = remeberMe;
		this.id = id;
		this.name = name;
		this.username = username;
		this.displayName = displayName;
		this.email = email;
		this.instId = instId;
		this.instName = instName;
		this.passwordSetType = passwordSetType;
		this.authorities = authorities;
	}


	/**
	 * authentication认证构造JWT
	 * @param token
	 * @param authentication
	 * @param expiresIn
	 * @param refreshToken
	 */
	public AuthJwt(String token, Authentication  authentication,int expiresIn,String refreshToken) {
		SignedPrincipal principal = ((SignedPrincipal)authentication.getPrincipal());

		this.token = token;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;

		this.ticket = principal.getSessionId();
		this.id = principal.getUserInfo().getId();
		this.username = principal.getUserInfo().getUsername();
		this.name = this.username;
		this.displayName = principal.getUserInfo().getDisplayName();
		this.email = principal.getUserInfo().getEmail();
		this.mobile = principal.getUserInfo().getMobile();
		this.instId = principal.getUserInfo().getInstId();
		this.twoFactor = principal.getTwoFactor();
		this.passwordSetType = principal.getPasswordSetType();

		this.authorities = new ArrayList<>();
		for(GrantedAuthority grantedAuthority :authentication.getAuthorities()) {
			this.authorities.add(grantedAuthority.getAuthority());
		}
	}

}
