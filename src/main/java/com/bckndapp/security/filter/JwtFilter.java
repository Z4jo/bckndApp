package com.bckndapp.security.filter;

import com.bckndapp.security.authentication.JwtAuthentication;
import com.bckndapp.security.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
	private JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String headerName = "Authorization";
		boolean doesHeaderExists=request.getHeader(headerName)!=null&&request.getHeader(headerName).contains("Bearer");
		if (doesHeaderExists){
			String[]splitHeader=request.getHeader(headerName).split(" ");
			var authentication= new JwtAuthentication(splitHeader[1]);
			try {
				authentication = (JwtAuthentication) jwtProvider.authenticate(authentication);
				if(authentication.isAuthenticated()){
					response.addHeader(headerName, String.valueOf(authentication.getCredentials()));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}catch (AuthenticationException exception){
				exception.printStackTrace();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			filterChain.doFilter(request,response);
		}else{
			filterChain.doFilter(request,response);
		}
	}


}