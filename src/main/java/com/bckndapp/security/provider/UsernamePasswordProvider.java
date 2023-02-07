package com.bckndapp.security.provider;

import com.bckndapp.security.SecurityUser;
import com.bckndapp.security.authentication.UsernamePasswordAuthentication;
import com.bckndapp.service.JwtService;
import com.bckndapp.service.UserJpaDetailsService;
import com.bckndapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class UsernamePasswordProvider implements AuthenticationProvider {
	private final UserJpaDetailsService userService;
	private final JwtService jwtService;
	private final PasswordEncoder encoder;
	private String token;
	private Collection<? extends GrantedAuthority> authorities;
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		boolean areUserCredentialsOk=isUserLegit((String) authentication.getCredentials(),authentication.getName());
		var usernamePasswordAuthentication = new UsernamePasswordAuthentication(authentication.getName(),(String)authentication.getCredentials());
		if(areUserCredentialsOk){
			usernamePasswordAuthentication.setAuthenticated(true);
			usernamePasswordAuthentication.setAuthorities(authorities);
			usernamePasswordAuthentication.setDetails(token);
			return usernamePasswordAuthentication;
		}
		throw new BadCredentialsException("bad credentials");
	}

	private boolean isUserLegit(String credentials, String name) throws UsernameNotFoundException {
		SecurityUser securityUser = (SecurityUser)userService.loadUserByUsername(name);
		authorities= securityUser.getAuthorities();
		token = jwtService.encodeToken(securityUser);
		return encoder.matches(credentials,securityUser.getPassword());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}
}
