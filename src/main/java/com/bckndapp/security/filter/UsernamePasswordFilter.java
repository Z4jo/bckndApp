package com.bckndapp.security.filter;

import com.bckndapp.security.authentication.UsernamePasswordAuthentication;
import com.bckndapp.security.provider.UsernamePasswordProvider;
import com.bckndapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@AllArgsConstructor
@Component
public class UsernamePasswordFilter extends OncePerRequestFilter {
	private	UsernamePasswordProvider usernamePasswordProvider;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String headerName = "Authorization";
		boolean doesHeaderExist = request.getHeader(headerName)!=null&&request.getHeader(headerName).contains("Basic ");
		if(doesHeaderExist){
			String[] usernameAndPassword= extractHeader(request.getHeader(headerName));
			var authentication = new UsernamePasswordAuthentication(usernameAndPassword[0],usernameAndPassword[1]);
			try{
				UsernamePasswordAuthentication usernamePasswordAuthentication= (UsernamePasswordAuthentication) usernamePasswordProvider.authenticate(authentication);
				if(usernamePasswordAuthentication.isAuthenticated()){
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
					response.setHeader(headerName,(String) usernamePasswordAuthentication.getDetails());
					response.setStatus(HttpServletResponse.SC_OK);
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



	private String[] extractHeader(String header) {
		String []encodedHeader = header.split(" ");
		byte[] decodedHeader= Base64.getDecoder().decode(encodedHeader[1]);
		return new String(decodedHeader).split(":");
	}
}
