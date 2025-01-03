package com.example.userLogin.service;

import com.example.userLogin.dto.kakao.KakaoResponseDto;
import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import com.example.userLogin.entity.UserEntity;
import com.example.userLogin.kakao.KakaoApi;
import com.example.userLogin.jwt.JwtToken;
import com.example.userLogin.jwt.JwtTokenProvider;
import com.example.userLogin.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KakaoApi kakaoApi;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    //나중에 에러 코드 추가 지금은 런타임
    private UserEntity findByStudentNumberOrThrow(String studentNumber) {
        return userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new RuntimeException("해당 학번은 없습니다."));
    }

    @Override
    public KakaoResponseDto kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        try {
            // Access Token 가져오기
            String accessToken = kakaoApi.getAccessToken(code, request);
            Map<String, String> userInfo = kakaoApi.getUserInfo(accessToken);

            String email = userInfo.get("email");

            // 이메일 검증
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("이메일을 가져올 수 없습니다.");
            }

            // 사용자 조회
            Optional<UserEntity> existingUser = userRepository.findByUserEmail(email);

            if (existingUser.isPresent()) {
                this.setJwtTokenInHeader(email,response);
                return KakaoResponseDto.builder()
                        .email(email)
                        .responseCode("카카오 로그인 성공! 이미 회원입니다.")
                        .build();
            }

            // 새로운 사용자 회원가입
            UserEntity newUser = UserEntity.builder()
                    .userEmail(email)
                    .build();

            userRepository.save(newUser);

            return KakaoResponseDto.builder()
                    .email(email)
                    .responseCode("카카오 로그인 성공! 회원가입 완료.")
                    .build();
        } catch (IllegalArgumentException e) {
            return KakaoResponseDto.builder()
                    .responseCode("카카오 로그인 실패: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return KakaoResponseDto.builder()
                    .responseCode("카카오 로그인 실패: 알 수 없는 오류 발생")
                    .build();
        }
    }


    //일단 유효성 검사는 개나 줘버린 코드..


    @Override
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        try {
            // studentNumber를 String으로 받아와서 사용
            String studentNumber = userLoginRequestDto.getStudentNumber();

            // studentNumber로 사용자 조회
            Optional<UserEntity> findByStudentNumber = userRepository.findByStudentNumber(studentNumber);

            // 사용자 존재 여부 확인
            if (findByStudentNumber.isEmpty()) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            UserEntity user = findByStudentNumber.get();

            // 비밀번호 비교
            if (!passwordEncoder.matches(userLoginRequestDto.getUserPassword(), user.getUserPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword());

            JwtToken jwtToken = jwtTokenProvider.issueToken(authentication);

            return UserLoginResponseDto.builder()
                    .studentNumber(user.getStudentNumber())
                    .userName(user.getUserName())
                    .accessToken(jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .build();

        } catch (NumberFormatException e) {
            throw new RuntimeException("학번 형식이 잘못되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    public boolean isEmailDuplicated(String email) {
        return userRepository.findByUserEmail(email).isPresent();
    }


    @Override
    public void signUp(UserSignupRequestDto userSignupRequestDto, HttpServletResponse response) {

        // 중복 학번 체크
        if (userRepository.findByStudentNumber(userSignupRequestDto.getStudentNumber()).isPresent()) {
            throw new RuntimeException("이미 존재하는 학번입니다.");
        }

        // DTO를 엔티티로 변환하여 저장
        UserEntity user = UserEntity.builder()
                .studentNumber(userSignupRequestDto.getStudentNumber())
                .userPassword(passwordEncoder.encode(userSignupRequestDto.getUserPassword()))
                .userName(userSignupRequestDto.getUserName())
                .userEmail(userSignupRequestDto.getUserEmail())
                .userPhone(userSignupRequestDto.getUserPhone())
                .build();

        userRepository.save(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Authorization 헤더가 유효하지 않습니다.");
            }

            String accessToken = authorizationHeader.substring(7);
            if (!jwtTokenProvider.validateToken(accessToken)) {
                throw new RuntimeException("유효하지 않은 토큰입니다.");
            }

            System.out.println("사용자가 로그아웃되었습니다. 토큰: " + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

    public void setJwtTokenInHeader(String email, HttpServletResponse response) {
        Optional<UserEntity> user = userRepository.findByUserEmail(email);

        if (user.isEmpty()) {
            throw new RuntimeException("NOT FOUND USER");
        }

        // 기본 권한 설정
        String defaultAuthorities = "USER";

        // AccessToken 생성
        String accessToken = jwtTokenProvider.createAccessToken(email, defaultAuthorities);

        // 헤더에 토큰 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
    }
}