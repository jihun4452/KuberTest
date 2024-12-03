package com.example.userLogin.dto.user.request;

import lombok.*;

//lombok dependency추가
@Getter
@Data
@NoArgsConstructor
@ToString
public class UserSignupRequestDto { //회원 정보를 필드로 정의
    private String studentNumber; //
    private String userPassword; //
    private String userName;
    private String userEmail;
    private Integer userPhone;

}