package com.shimady.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT token pair returned on successful auth")
public class JwtResponse {

    @Schema(description = "Token type", example = "Bearer")
    private final String type = "Bearer";

    @Schema(description = "JWT access token", example = "<access-token>")
    private String accessToken;
    
    @Schema(description = "JWT refresh token", example = "<refresh-token>")
    private String refreshToken;
}
