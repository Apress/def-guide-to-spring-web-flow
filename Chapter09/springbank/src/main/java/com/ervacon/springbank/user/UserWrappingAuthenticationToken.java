package com.ervacon.springbank.user;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.AbstractAuthenticationToken;

public class UserWrappingAuthenticationToken extends AbstractAuthenticationToken {
	
	private User user;
	
	public UserWrappingAuthenticationToken(User user) {
		super(new GrantedAuthority[] { new GrantedAuthorityImpl( "ROLE_USER" )});
		setDetails(user);
		this.user = user;
	}

	public Object getCredentials() {
		return user.getPassword();
	}
	
	public Object getPrincipal() {
		return user.getUserName();
	}
}
