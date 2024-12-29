package com.example.userLogin.service;

import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService {
    void signUp(UserSignupRequestDto requestDto, HttpServletResponse response);
    UserLoginResponseDto login(UserLoginRequestDto requestDto, HttpServletResponse response);

}
