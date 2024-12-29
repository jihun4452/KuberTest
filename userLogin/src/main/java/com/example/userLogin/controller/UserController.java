package com.example.userLogin.controller;

import com.example.userLogin.dto.kakao.KakaoResponseDto;
import com.example.userLogin.dto.user.request.UserLoginRequestDto;
import com.example.userLogin.dto.user.request.UserSignupRequestDto;
import com.example.userLogin.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserServiceImpl userService;

  @Operation(summary = "카카오 로그인API")
  @GetMapping("/login/kakao")
  public KakaoResponseDto kakaoLogin(@RequestParam("code") String authorizeCode, HttpServletRequest request, HttpServletResponse response) {
    return userService.kakaoLogin(authorizeCode, request, response);
  }

  @Operation(summary = "유저 회원가입")
  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserSignupRequestDto userSignupRequestDto, HttpServletResponse response) {
    userService.signUp(userSignupRequestDto,response);
    return ResponseEntity.ok("Signup Successful");
  }

  @Operation(summary = "유저 로그인")
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
    userService.login(userLoginRequestDto,response);
    return ResponseEntity.ok("Login Successful");
  }

}