package com.example.userLogin.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class UserLoginRequestDto {
    @Schema(description = "학번")
    private String studentNumber; // 학번

    @Schema(description = "일반 로그인 패스워드")
    private String userPassword; // 패스워드
}
