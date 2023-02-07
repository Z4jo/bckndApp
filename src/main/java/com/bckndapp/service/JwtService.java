package com.bckndapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bckndapp.security.SecurityUser;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
	public DecodedJWT decodeJWT(String token){
		DecodedJWT decodedJWT;
		try {
			decodedJWT = getDecodedToken(token);
			return decodedJWT;
		}
		catch(JWTVerificationException exception){
			//TODO:LOG
			exception.printStackTrace();
		}
		return null;
	}

	private DecodedJWT getDecodedToken(String token) throws JWTVerificationException {
		Algorithm algorithm= Algorithm.HMAC512("secret");
		JWTVerifier jwtVerifier= JWT.require(algorithm)
				.build();
		return jwtVerifier.verify(token);
	}

	public String encodeToken(SecurityUser securityUser){
		Algorithm algorithm = Algorithm.HMAC512("secret");
		return JWT.create()
				.withSubject(securityUser.getUsername())
				.withExpiresAt(plusThirtyMinutes())
				.sign(algorithm);
	}

	private Instant plusThirtyMinutes() {
		return Instant.ofEpochSecond(Instant.now().getEpochSecond()+1800);
	}
}

























