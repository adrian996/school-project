package com.example.mylittleproject.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.mylittleproject.model.MyUserDetails;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component

//source: https://www.bezkoder.com/spring-boot-jwt-authentication/
public class JwtUtils {

	@Value("secret")
	private String jwtSecret;
	@Value("86400000")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {
		MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();
		
        String token = Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
				//System.out.println(token);
		return token;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		System.out.println(authToken);
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.info("Invalid JWT signature: {}" + e.getMessage());
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			log.info("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.info("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty: {}" + e.getMessage());
		}
		return false;
	}
}
