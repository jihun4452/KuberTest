package com.example.userLogin.dto.request;

import lombok.*;

@Getter
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserLoginRequestDto {
    private String studentNumber;
    private String userPassword;
}
