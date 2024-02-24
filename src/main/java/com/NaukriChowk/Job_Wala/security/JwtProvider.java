package com.NaukriChowk.Job_Wala.security;

import com.NaukriChowk.Job_Wala.cache.LoggedOutJwtTokenCache;
import com.NaukriChowk.Job_Wala.event.OnUserLogoutSuccessEvent;
import com.NaukriChowk.Job_Wala.exception.InvalidTokenRequestException;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.service.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);


    @Autowired
    private LoggedOutJwtTokenCache loggedOutJwtTokenCache;

    private static final String SECRET_KEY_ALGORITHM = "HmacSHA256";
    private static final int SECRET_KEY_LENGTH_BYTES = 32; // 256 bits

    private static SecretKey secretKey = generateSecretKey();

    private static SecretKey generateSecretKey() {

            SecureRandom secureRandom = new SecureRandom();
            byte[] keyBytes = new byte[SECRET_KEY_LENGTH_BYTES];
            secureRandom.nextBytes(keyBytes);
            return new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM);

    }


    public static String generateJwtToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setId(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenFromUser(User user) {
        Instant expiryDate = Instant.now().plusMillis(3600000);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setId(Long.toString(user.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Date getTokenExpiryFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            validateTokenIsNotForALoggedOutDevice(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    private void validateTokenIsNotForALoggedOutDevice(String authToken) {
        OnUserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutJwtTokenCache.getLogoutEventForToken(authToken);
        if (previouslyLoggedOutEvent != null) {
            String userEmail = previouslyLoggedOutEvent.getUserEmail();
            Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]. Please login again", userEmail, logoutEventDate);
            throw new InvalidTokenRequestException("JWT", authToken, errorMessage);
        }
    }

    public long getExpiryDuration() {
        return 3600000;
    }
}
