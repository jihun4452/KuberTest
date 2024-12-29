package com.example.userLogin.service;

import com.example.userLogin.domain.User;
import com.example.userLogin.dto.kakao.KakaoResponseDto;
import com.example.userLogin.dto.user.request.UserLoginRequestDto;
import com.example.userLogin.dto.user.request.UserSignupRequestDto;
import com.example.userLogin.dto.user.response.UserLoginResponseDto;
import com.example.userLogin.kakao.KakaoApi;
import com.example.userLogin.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KakaoApi kakaoApi;


    //나중에 에러 코드 추가 지금은 런타임
    private User findByStudentNumberOrThrow(String studentNumber) {
        return userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new RuntimeException("해당 학번은 없습니다."));
    }

    @Override
    public KakaoResponseDto kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        try {
            String access_token = kakaoApi.getAccessToken(code, request);
            Map<String, String> userInfo = kakaoApi.getUserInfo(access_token);

            String email = userInfo.get("email");

            if (email == null ) {
                throw new RuntimeException("이메일 또는 이름을 가져올 수 없습니다."); //에러 코드가 없어 ㅠㅠ
            }

            UserSignupRequestDto requestDto =UserSignupRequestDto.builder()
                    .userEmail(email)
                    .build();

            signUp(requestDto,response);

            return KakaoResponseDto.builder()
                    .email(email)
                    .responseCode("카카오 로그인 성공~!")
                    .build();
        } catch (Exception e) {
            return KakaoResponseDto.builder()
                    .responseCode("카카오 로그인 실패: " + e.getMessage())
                    .build();
        }
    } //일단 유효성 검사는 개나 줘버린 코드..


    @Override
    public void signUp(UserSignupRequestDto requestDto, HttpServletResponse response) {
        if(userRepository.findByStudentNumber(requestDto.getStudentNumber()).isPresent()){
            throw new RuntimeException("이미 존재하는 학번입니다.");
        }

        User user = requestDto.toEntity();
        userRepository.save(user);
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto requestDto, HttpServletResponse response) {
        User user = findByStudentNumberOrThrow(requestDto.getStudentNumber());

        if(!user.getUserPassword().equals(requestDto.getUserPassword())){
            throw new RuntimeException("비밀번호가 일치하지않습니다.");
        }

        return UserLoginResponseDto.builder()
                .responseCode("로그인 되었습니다.")
                .build();
    }

}
