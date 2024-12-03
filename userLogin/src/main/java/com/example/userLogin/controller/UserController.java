package com.example.userLogin.controller;

import com.example.userLogin.dto.user.request.UserLoginRequestDto;
import com.example.userLogin.dto.user.request.UserSignupRequestDto;
import com.example.userLogin.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserServiceImpl userService;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestParam UserSignupRequestDto userSignupRequestDto, HttpServletResponse response) {
    userService.signUp(userSignupRequestDto,response);
    return ResponseEntity.ok("Signup Successful");
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestParam UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
    userService.login(userLoginRequestDto,response);
    return ResponseEntity.ok("Login Successful");
  }

}