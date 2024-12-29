package com.example.userLogin.controller;

import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import com.example.userLogin.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/user/join")
    public String join() {
        return "join";
    }

    @PostMapping("/user/join")
    public String save(@ModelAttribute UserSignupRequestDto userSignupRequestDto) {
        userService.signUp(userSignupRequestDto);

        return "main";
    }

    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    @PostMapping("/user/login")
    public String login(@ModelAttribute UserLoginRequestDto userLoginRequestDto, HttpSession session) {
        UserLoginResponseDto loginResult = userService.login(userLoginRequestDto);
        if(loginResult != null) {
            session.setAttribute("user", loginResult.getStudentNumber());
            session.removeAttribute("loginFailed");
            return "main";
        } else {
            session.setAttribute("loginFailed", true);
            return "login";
        }

    }


}