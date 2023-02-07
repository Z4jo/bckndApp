package com.bckndapp.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthentication implements Authentication {

	private boolean authentication;
	private Collection<? extends  GrantedAuthority>authorities;
	private final String TOKEN;


	public void setAuthority(Collection<? extends  GrantedAuthority>authorities){
		this.authorities=authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Object getCredentials() {
		return TOKEN;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return this.authentication;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authentication=isAuthenticated;
	}

	@Override
	public String getName() {
		return null;
	}


}
