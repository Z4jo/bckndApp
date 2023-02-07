package com.bckndapp.security;

import com.bckndapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {
	private Role role;
	@Override
	public String getAuthority() {
		return String.valueOf(role);
	}
}
