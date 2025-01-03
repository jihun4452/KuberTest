package com.example.userLogin.controller;

import com.example.userLogin.dto.kakao.KakaoResponseDto;
import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import com.example.userLogin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

  private final UserService userService;

  @Operation(summary = "카카오 로그인API")
  @GetMapping("/login/kakao")
  public KakaoResponseDto kakaoLogin(@RequestParam("code") String authorizeCode, HttpServletRequest request, HttpServletResponse response) {
    return userService.kakaoLogin(authorizeCode, request, response);
  }

  @Operation(summary = "유저 회원가입")
  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserSignupRequestDto userSignupRequestDto, HttpServletResponse response) {
    userService.signUp(userSignupRequestDto,response);
    return ResponseEntity.status(HttpStatus.CREATED).body("Signup Successful");
  }

  @Operation(summary = "유저 로그인")
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
    UserLoginResponseDto userLoginResponseDto = userService.login(userLoginRequestDto,response);

    return ResponseEntity.ok()
            .header("Authorization", "Bearer " + userLoginResponseDto.getAccessToken())
            .header("RefreshToken", userLoginResponseDto.getRefreshToken())
            .body("Login Successful");
  }

  @Operation(summary = "유저 로그아웃")
  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    userService.logout(request);
    return ResponseEntity.ok("로그아웃 되었습니다.");
  }


  @Operation(summary = "이메일 중복 확인")
  @GetMapping("/Duplicated")
  public ResponseEntity<?> isEmailDuplicated(@RequestParam String email) {
    boolean isEmailDuplicated = userService.isEmailDuplicated(email);
    return ResponseEntity.ok("이메일 중복 여부 : " + isEmailDuplicated);
  }

}