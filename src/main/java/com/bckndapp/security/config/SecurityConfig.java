package com.bckndapp.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bckndapp.security.filter.JwtFilter;
import com.bckndapp.security.filter.UsernamePasswordFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

	private JwtFilter jwtFilter;
	private UsernamePasswordFilter usernamePasswordFilter;

	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		return http
				.cors(CorsConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAt(usernamePasswordFilter,UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests()
				.requestMatchers("/*/public/**").permitAll()
				.and()
				.authorizeHttpRequests()
				.requestMatchers("/*/private/**").hasAnyAuthority("CLIENT","STAFF","ADMIN")
				.requestMatchers("/*/staff/**").hasAnyAuthority("STAFF","ADMIN")
				.requestMatchers("/*/admin/**").hasAnyAuthority("ADMIN").anyRequest().authenticated()
				.and()
				.build();
	}



}














