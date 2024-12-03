package com.example.userLogin.dto.user.request;

import com.example.userLogin.domain.User;
import lombok.*;

@Getter
@Data
@NoArgsConstructor
@ToString
public class UserSignupRequestDto {
    private String studentNumber;
    private String userPassword;
    private String userName;
    private String userEmail;
    private Integer userPhone;

    public User toEntity(){
        return User.builder()
                .studentNumber(studentNumber)
                .userPassword(userPassword)
                .userName(userName)
                .userEmail(userEmail)
                .userPhone(userPhone)
                .build();
    }
}