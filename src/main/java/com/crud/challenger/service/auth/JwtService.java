/**
 * 
 */
package com.crud.challenger.service.auth;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * @author Tomás González
 * 
 */
@Service
public class JwtService {
	
	@Value("${security.jwt.expiration-in-minutes}")
	private Long EXPIRATION_IN_MINUTES;
	
	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY;

	public String generateToken(String userName, Map<String, Object> extraClaims) {
		
		Date issedAt = new Date(System.currentTimeMillis());
		Date expirationDate = new Date(issedAt.getTime() + (EXPIRATION_IN_MINUTES) * 60 * 1000);
		
		return Jwts.builder()
				.header()
					.type("JWT")
					.and()
				.subject(userName)
				.issuedAt(issedAt)
				.expiration(expirationDate)
				.claims(extraClaims)
				.signWith(generatedKey(), Jwts.SIG.HS256)
				.compact();
	}
	
	private SecretKey generatedKey() {
		byte[] secretDecoded = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(secretDecoded);
	}

	public Claims extractAllClaims(String jwt) {
		return Jwts.parser().verifyWith(generatedKey()).build().parseSignedClaims(jwt).getPayload();
	}
	


}
