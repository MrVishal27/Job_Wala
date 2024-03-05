package com.NaukriChowk.Job_Wala.controller;

import com.NaukriChowk.Job_Wala.dto.*;
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
        return new ResponseEntity<>(authService.registerUser(signUpForm),HttpStatus.CREATED);
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        return new ResponseEntity<>(authService.loginUser(loginForm),HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(tokenRefreshRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtProvider.generateToken( user);

        return ResponseEntity.ok(new RefreshTokenResponse(accessToken));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        if (otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found with this email: " + request.getEmail()));
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("OTP is valid. User is verified!");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP. Please try again.");
        }
    }

    @PostMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestBody RegenerateOtp regenerate) {
        return new ResponseEntity<>(otpService.regenerateOtp(regenerate), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return  new ResponseEntity<>(authService.changePassword(changePasswordRequest),HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    private ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        return  new ResponseEntity<>(authService.forgetPassword(forgetPasswordRequest),HttpStatus.OK);
    }

    @PostMapping("/verify-and-change-password")
    public ResponseEntity<?> verifyAndChangePassword(@RequestBody VerifyAndChangePassword request) {
        return authService.verifyAndChangePassword(request);
    }
}
