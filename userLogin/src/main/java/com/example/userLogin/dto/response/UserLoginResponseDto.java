package com.example.userLogin.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginResponseDto {
    private String studentNumber;
    private String userName;
    private String accessToken;
    private String refreshToken;
}
