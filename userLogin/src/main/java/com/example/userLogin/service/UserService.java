package com.example.userLogin.service;

import com.example.userLogin.dto.user.request.UserLoginRequestDto;
import com.example.userLogin.dto.user.request.UserSignupRequestDto;
import com.example.userLogin.dto.user.response.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void signUp(UserSignupRequestDto requestDto, HttpServletResponse response);
    UserLoginResponseDto login(UserLoginRequestDto requestDto, HttpServletResponse response);
}
