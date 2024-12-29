package com.example.userLogin.service;

import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto);

    void signUp(UserSignupRequestDto userSignupRequestDto);

}
