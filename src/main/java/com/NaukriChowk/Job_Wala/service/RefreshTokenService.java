package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.exception.TokenRefreshException;
import com.NaukriChowk.Job_Wala.model.RefreshToken;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.repo.RefreshTokenRepository;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;



    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            long refreshTokenValidity = 3 * 60 * 60 * 1000;
            refreshToken = new RefreshToken();
            refreshToken.setExpirationTime(Instant.now().plusMillis(refreshTokenValidity));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));

        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token expired");
        }

        return refToken;
    }

}
