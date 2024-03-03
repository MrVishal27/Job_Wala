package com.NaukriChowk.Job_Wala.controller;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.OtpVerificationRequest;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import com.NaukriChowk.Job_Wala.dto.TokenRefreshRequest;
import com.NaukriChowk.Job_Wala.model.RefreshToken;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import com.NaukriChowk.Job_Wala.response.RefreshTokenResponse;
import com.NaukriChowk.Job_Wala.security.JwtProvider;
import com.NaukriChowk.Job_Wala.service.AuthService;
import com.NaukriChowk.Job_Wala.service.OtpService;
import com.NaukriChowk.Job_Wala.service.RefreshTokenServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final OtpService otpService;
    private final RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpForm)  {
        return authService.registerUser(signUpForm);
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        return authService.loginUser(loginForm);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(tokenRefreshRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtProvider.generateToken( user);

        return ResponseEntity.ok(new RefreshTokenResponse(accessToken));
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpVerificationRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + request.getEmail()));

        String email = request.getEmail();
        String enteredOtp = request.getOtp();

        if (otpService.verifyOtp(email, enteredOtp)
                && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (2*60) ){

            user.setVerified(true);
            userRepository.save(user);
            return "OTP is valid. User is verified!";
        }
        else if(otpService.verifyOtp(email, enteredOtp)
                && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() > (2*60)){

            return "Otp is Expired. Please re-generate otp.";
        }
        else {
            return "Invalid OTP. User verification failed.";
        }
    }

    @PostMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestBody String email) {
        return new ResponseEntity<>(otpService.regenerateOtp(email), HttpStatus.OK);
    }
}
