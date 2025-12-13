package com.shimady.auth.controller;

import com.shimady.auth.config.props.JwtProperties;
import com.shimady.auth.exception.AppError;
import com.shimady.auth.exception.ValidationError;
import com.shimady.auth.model.dto.JwtResponse;
import com.shimady.auth.model.dto.SignInJwtRequest;
import com.shimady.auth.model.dto.SignUpJwtRequest;
import com.shimady.auth.model.dto.UserResponse;
import com.shimady.auth.service.AuthService;
import com.shimady.auth.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and session management endpoints")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "Authentication or authorization error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AppError.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AppError.class)))
})
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user", description = "Returns the profile of the currently authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current user profile",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized (no/invalid token)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class)))
    })
    public UserResponse getCurrentUser() {
        return authService.getCurrentUser();
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up", description = "Registers a new user and sets JWT tokens in cookies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Signed up successfully (cookies set)"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "404", description = "Group not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "409", description = "User with such email already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class)))
    })
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpJwtRequest request) {
        JwtResponse response = authService.signUp(request);
        return buildResponse(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and sets JWT tokens in cookies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated successfully (cookies set)"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class)))
    })
    public ResponseEntity<Void> signIn(@Valid @RequestBody SignInJwtRequest request) {
        JwtResponse response = authService.authenticate(request);
        return buildResponse(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refreshes JWT tokens using the refresh token cookie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens refreshed (cookies set)"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class)))
    })
    public ResponseEntity<Void> refreshToken(@Parameter(description = "Refresh token cookie value") @CookieValue(value = "${jwt.token.refresh.cookie-name}") String refreshToken) {
        JwtResponse response = authService.refreshToken(refreshToken);
        return buildResponse(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Removes refresh token and clears auth cookies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully (cookies cleared)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (no/invalid token)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class)))
    })
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        JwtUtils.removeTokenCookie(jwtProperties.getAccess().getCookieName())
                )
                .header(
                        HttpHeaders.SET_COOKIE,
                        JwtUtils.removeTokenCookie(jwtProperties.getRefresh().getCookieName())
                )
                .build();
    }

    private ResponseEntity<Void> buildResponse(JwtResponse response) {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        JwtUtils.createTokenCookie(
                                jwtProperties.getAccess().getCookieName(),
                                response.getAccessToken(),
                                jwtProperties.getAccess().getExpiration()
                        )
                )
                .header(
                        HttpHeaders.SET_COOKIE,
                        JwtUtils.createTokenCookie(
                                jwtProperties.getRefresh().getCookieName(),
                                response.getRefreshToken(),
                                jwtProperties.getRefresh().getExpiration()
                        )
                )
                .build();
    }
}
