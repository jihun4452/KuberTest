package com.example.userLogin.entity;

import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String studentNumber;

    @Column
    private String userPassword;

    @Column
    private String userName;

    @Column(unique = true)
    private String userEmail;

    @Column
    private Integer userPhone;

    //lombok 어노테이션으로 getter,setter,생성자,toString 메서드 생략 가능
    public UserEntity( String studentNumber, String userPassword, String userName, String userEmail, Integer userPhone) {
        this.studentNumber = studentNumber;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

    public static UserEntity toUserLoginEntity(UserLoginRequestDto userLoginRequestDto) {
        return UserEntity.builder()
                .studentNumber(userLoginRequestDto.getStudentNumber())
                .userPassword(userLoginRequestDto.getUserPassword())
                .build();
    }

    public static UserEntity toUserSignUpEntity(UserSignupRequestDto userSignupRequestDto) {
        return UserEntity.builder()
                .studentNumber(userSignupRequestDto.getStudentNumber())
                .userPassword(userSignupRequestDto.getUserPassword())
                .userName(userSignupRequestDto.getUserName())
                .userEmail(userSignupRequestDto.getUserEmail())
                .userPhone(userSignupRequestDto.getUserPhone())
                .build();
    }
}
