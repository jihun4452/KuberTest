package com.example.userLogin.service;

import com.example.userLogin.dto.kakao.KakaoResponseDto;
import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService {
    KakaoResponseDto kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response);
    void signUp(UserSignupRequestDto requestDto, HttpServletResponse response);
    UserLoginResponseDto login(UserLoginRequestDto requestDto, HttpServletResponse response);
    boolean isEmailDuplicated(String email);
    void logout(HttpServletRequest request);
}
