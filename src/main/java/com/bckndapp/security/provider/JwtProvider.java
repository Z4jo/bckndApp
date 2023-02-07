package com.bckndapp.security.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bckndapp.security.SecurityUser;
import com.bckndapp.security.authentication.JwtAuthentication;
import com.bckndapp.service.JwtService;
import com.bckndapp.service.UserJpaDetailsService;
import com.bckndapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;

@Component
@AllArgsConstructor
public class JwtProvider implements AuthenticationProvider {
	private JwtService jwtService;
	private UserJpaDetailsService userService;
	private Collection<? extends GrantedAuthority> authorities;
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		DecodedJWT decodedJWT= jwtService.decodeJWT(String.valueOf(authentication.getCredentials()));
		Instant tokenExpiration=decodedJWT.getExpiresAtAsInstant();
		boolean isTokenExpired=tokenExpiration.isBefore(Instant.now());
		if(isTokenExpired){
			throw new BadCredentialsException("token is expired");
		}
		String newToken=addTimeToToken(decodedJWT);
		var jwtAuthentication= new JwtAuthentication(newToken);
		jwtAuthentication.setAuthenticated(true);
		jwtAuthentication.setAuthority(authorities);
		return jwtAuthentication;
	}

	private String addTimeToToken(DecodedJWT decodedJWT) {
	    SecurityUser securityUser= (SecurityUser) userService.loadUserByUsername(decodedJWT.getSubject());
		authorities =securityUser.getAuthorities();
		return jwtService.encodeToken(securityUser);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}
}
