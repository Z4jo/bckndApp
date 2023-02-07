package com.bckndapp.security;

import com.bckndapp.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {
	private final User USER;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		var authority = new SecurityAuthority(USER.getRole());
		return List.of(authority);
	}

	@Override
	public String getPassword() {
		return this.USER.getPassword();
	}

	@Override
	public String getUsername() {
		return this.USER.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
