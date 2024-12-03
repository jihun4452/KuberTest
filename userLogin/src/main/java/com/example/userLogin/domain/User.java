package com.example.userLogin.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

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

    @Builder
    public User( String studentNumber, String userPassword, String userName, String userEmail, Integer userPhone) {
        this.studentNumber = studentNumber;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }
}