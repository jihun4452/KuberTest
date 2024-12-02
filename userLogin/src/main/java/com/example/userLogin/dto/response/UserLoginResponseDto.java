package com.example.userLogin.dto.response;

import com.example.userLogin.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private Long studentNumber;
    private String userName;

    public UserLoginResponseDto(UserEntity userEntity) {
        this.studentNumber = Long.valueOf(userEntity.getStudentNumber());
        this.userName = userEntity.getUserName();
    }
}
