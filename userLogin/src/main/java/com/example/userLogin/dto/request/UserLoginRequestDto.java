package com.example.userLogin.dto.request;

import lombok.*;

@Data
@RequiredArgsConstructor
public class UserLoginRequestDto {
    private String studentNumber;
    private String userPassword;
}
