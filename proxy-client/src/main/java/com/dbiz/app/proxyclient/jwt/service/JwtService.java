package com.dbiz.app.proxyclient.jwt.service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import com.dbiz.app.proxyclient.business.auth.model.response.AuthRespDto;
import com.dbiz.app.proxyclient.jwt.domain.AuthUserDetails;
import com.dbiz.app.proxyclient.jwt.domain.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {
	
	String extractUsername(final String token);
	Date extractExpiration(final String token);
	Date extractIssuedAt(final String token);
	Integer extractdTenantIdExpired(final String token);
	<T> T extractClaims(final String token, final Function<Claims, T> claimsResolver);

	String generateAccessToken(final CustomUserDetails userDetails, Integer dOrgId, String language);

	String generateRefreshToken(final CustomUserDetails userDetails, Integer dOrgId, String language);

	String refreshExpiredToken(final String expiredToken);

	Boolean validateToken(final String token, final UserDetails userDetails);

	Integer extractTenantId(final String token);

	AuthRespDto generateToken(final AuthUserDetails userDetails);

	String extractIssuer(final String token);
}










