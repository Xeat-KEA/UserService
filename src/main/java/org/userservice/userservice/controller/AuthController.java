package org.userservice.userservice.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.userservice.userservice.domain.AuthRole;
import org.userservice.userservice.dto.auth.SignupRequest;
import org.userservice.userservice.dto.auth.SignupResponse;
import org.userservice.userservice.jwt.JwtToken;
import org.userservice.userservice.service.AuthService;
import org.userservice.userservice.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final UserService userService;
    private final AuthService authService;

    @Override
    @GetMapping("/cookie-to-header")
    public ResponseEntity<?> cookieToHeader(@CookieValue(name = "Authorization", required = false) String token, HttpServletResponse response) {
        Claims claims = authService.validateAndExtractClaims(token, AuthRole.ROLE_USER_B);
        String bearerToken = authService.createBearerToken(claims.getSubject(), AuthRole.ROLE_USER_B);
        response.addHeader("Authorization", bearerToken);
        return ResponseEntity.ok(new JwtToken(bearerToken, null));
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Validated @RequestBody SignupRequest signupRequest,
            @CookieValue(name = "Authorization", required = false) String token,
            HttpServletResponse response) {

        Claims claims = authService.validateAndExtractClaims(token, AuthRole.ROLE_USER_A);
        String userId = claims.getSubject();
        AuthRole newRole = userService.signup(signupRequest, userId);
        String bearerToken = authService.createBearerToken(userId, newRole);
        //TODO: refresh token 발급 로직 추가
        response.addHeader("Authorization", bearerToken);
        return ResponseEntity.ok(new SignupResponse(userId, newRole,  new JwtToken(bearerToken, null)));
    }
}
