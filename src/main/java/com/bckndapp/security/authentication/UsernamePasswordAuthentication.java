package com.bckndapp.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@RequiredArgsConstructor
public class UsernamePasswordAuthentication implements Authentication {
	private final String USERNAME;
	private final String PASSWORD;
	private Collection<? extends GrantedAuthority> authorities;
	private String details;
	private boolean isAuthenticated;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Object getCredentials() {
		return PASSWORD;
	}

	@Override
	public Object getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.isAuthenticated =isAuthenticated;
	}

	@Override
	public String getName() {
		return USERNAME;
	}
}
