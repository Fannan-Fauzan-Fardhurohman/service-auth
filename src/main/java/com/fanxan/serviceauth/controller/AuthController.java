package com.fanxan.serviceauth.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fanxan.serviceauth.exception.TokenRefreshException;
import com.fanxan.serviceauth.model.dto.request.LoginRequest;
import com.fanxan.serviceauth.model.dto.response.JwtResponse;
import com.fanxan.serviceauth.model.dto.response.TokenRefreshResponse;
import com.fanxan.serviceauth.model.dto.response.UserDetailsImpl;
import com.fanxan.serviceauth.model.entity.RefreshToken;
import com.fanxan.serviceauth.model.entity.TokenRefreshRequest;
import com.fanxan.serviceauth.service.RefreshTokenService;
import com.fanxan.serviceauth.service.UserService;
import com.fanxan.serviceauth.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Login",
            responses = {
                    @ApiResponse(description = HttpConstant.OK, responseCode = HttpConstant.CODE_OK, useReturnTypeSchema = true),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Bad Request", responseCode = "400")
            }
    )
    public ResponseEntity<BaseResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new BaseResponse<>("Login successful", "SUCCESS", 200, jwtResponse));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse<>("Invalid username or password", "UNAUTHORIZED", 401, null));
        } catch (Exception e) {
            log.error("Login error: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>("Login failed", "ERROR", 400, null));
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh Token is not in database"));
    }

}
