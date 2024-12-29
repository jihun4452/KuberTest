package com.example.userLogin.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String studentNumber;
    private String userName;
    private String accessToken;
    private String refreshToken;
}
