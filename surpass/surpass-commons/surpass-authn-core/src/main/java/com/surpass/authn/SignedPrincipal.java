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






package com.surpass.authn;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.surpass.authn.session.Session;
import com.surpass.entity.client.ClientResolve;
import com.surpass.entity.idm.UserInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录后颁发签名的当事人，包含用户信息，当前会话，用户角色，用户应用权限等
 *
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
public class SignedPrincipal implements  UserDetails {
    private static final long serialVersionUID = -110742975439268030L;

    String 	sessionId;

    String 	style;

    String  userId;

    String  username;

    String 	mobile;

    String 	email;

    String instId;

    int   	twoFactor;

    int 	passwordSetType;

    String 	deviceId;

    UserInfo userInfo;

    ClientResolve clientResolve;

    UserDetails userDetails;

    List<GrantedAuthority> grantedAuthority;

    List<GrantedAuthority> grantedApps;

    boolean authenticated;

    boolean roleAdministrators;

	boolean accountNonExpired;

	boolean accountNonLocked;

	boolean credentialsNonExpired;

	boolean enabled;

    /**
     * SignPrincipal.
     */
    public SignedPrincipal(UserInfo user) {
        this.userInfo = user;
        this.authenticated = true;
        this.passwordSetType = user.getPasswordSetType();
        this.accountNonExpired = true;
        this.accountNonLocked  = true;
        this.credentialsNonExpired =true;
        this.enabled = true;
    }

    public SignedPrincipal(UserInfo user,Session session) {
    	this.userId = user.getId();
    	this.username = user.getUsername();
    	this.instId = user.getInstId();
    	this.email = user.getEmail();
    	this.mobile = user.getMobile();
    	this.passwordSetType = user.getPasswordSetType();
        this.userInfo = user;
        this.authenticated = true;
        this.accountNonExpired = true;
        this.accountNonLocked  = true;
        this.credentialsNonExpired =true;
        this.enabled = true;
        this.sessionId = session.getId();
        this.userInfo.setSessionId(session.getId());
    }

    /**
     * SigninPrincipal.
     */
    public SignedPrincipal(UserDetails userDetails) {
        this.userDetails = userDetails;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }

	public void clearTwoFactor() {
		this.twoFactor = 0;
	}

	@Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

	@Override
    public String getUsername() {
        if(this.userInfo != null) {
            return this.userInfo.getUsername();
        }else {
            return this.userDetails.getUsername();
        }
    }

    @Override
    public String getPassword() {
        if(this.userInfo != null) {
            return this.userInfo.getPassword();
        }else {
            return this.userDetails.getPassword();
        }
    }

}
