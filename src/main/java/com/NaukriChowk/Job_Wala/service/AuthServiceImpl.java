package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import com.NaukriChowk.Job_Wala.dto.TokenRefreshRequest;
import com.NaukriChowk.Job_Wala.exception.TokenRefreshException;
import com.NaukriChowk.Job_Wala.model.*;
import com.NaukriChowk.Job_Wala.repo.RoleRepository;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import com.NaukriChowk.Job_Wala.response.LoginResponse;
import com.NaukriChowk.Job_Wala.response.RefreshTokenResponse;
import com.NaukriChowk.Job_Wala.response.RegisterResponse;
import com.NaukriChowk.Job_Wala.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDeviceService userDeviceService;
    @Override
    public RegisterResponse registerEmployee(SignUpForm signUpForm) throws Exception {

        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            throw new Exception("Fail -> Email is already in use!");
        }

        // Creating user's account
        User user = new User();
        user.setFirstName(signUpForm.getFirstName());
        user.setFirstName(signUpForm.getLastName());
        user.setEmail(signUpForm.getEmail());
        user.setAge(signUpForm.getAge());
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

        Set<String> strRoles = signUpForm.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(adminRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        user.activate();
        User result = userRepository.save(user);

//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/user/me")
//                .buildAndExpand(result.getId()).toUri();

        return new RegisterResponse("success");

    }


    @Override
    public LoginResponse loginEmployee(LoginForm loginForm) {

        User user = userRepository.findByEmail(loginForm.getEmail())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User not found."));

        if (user.getActive()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.getEmail(),
                            loginForm.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = JwtProvider.generateJwtToken(authentication);

            // Delete existing refresh token if it exists
            userDeviceService.findByUserId(user.getId())
                    .map(UserDevice::getRefreshToken)
                    .map(RefreshToken::getId)
                    .ifPresent(refreshTokenService::deleteById);

            // Create new user device and refresh token
            UserDevice userDevice = userDeviceService.createUserDevice(loginForm.getDeviceInfo());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken();
            userDevice.setUser(user);
            userDevice.setRefreshToken(refreshToken);
            refreshToken.setUserDevice(userDevice);
            refreshToken = refreshTokenService.save(refreshToken);


            return new LoginResponse("success", jwtToken, refreshToken.getToken(), jwtProvider.getExpiryDuration());
        }else{
            return new LoginResponse("User not active", null, null, 0L);
        }
    }

    @Override
    public RefreshTokenResponse refresh(TokenRefreshRequest tokenRefreshRequest) {

        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        Optional<String> token = Optional.of(refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userDeviceService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserDevice)
                .map(UserDevice::getUser)
                .map(u -> jwtProvider.generateTokenFromUser(u))
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Missing refresh token in database.Please login again")));
        return new RefreshTokenResponse(tokenRefreshRequest.getRefreshToken(),jwtProvider.getExpiryDuration());
    }

}
