package com.example.userLogin.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class UserLoginRequestDto {
    private String studentNumber; //
    private String userPassword; //
}
