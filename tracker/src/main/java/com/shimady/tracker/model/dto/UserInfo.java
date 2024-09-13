package com.shimady.tracker.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "My Username")
    private String username;

    @Schema(example = "email@mail.com")
    private String email;

    @Schema(example = "+76969696969")
    private String phoneNumber;
}
