package com.surpass.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.surpass.constants.ConstsToken;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccessToken extends Message<Object>{
	@JsonProperty(ConstsToken.ACCESS_TOKEN)
	String accessToken;
	@JsonProperty(ConstsToken.EXPIRES_IN)
	int expiresIn = 3600 * 2;
	@JsonProperty(ConstsToken.REFRESH_TOKEN)
	String refreshToken;
	@JsonProperty(ConstsToken.REFRESH_TOKEN_EXPRISE)
	int refreshTokenExprise = 3600 * 8;
}
