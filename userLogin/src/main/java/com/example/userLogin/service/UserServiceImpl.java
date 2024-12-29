package com.example.userLogin.service;

import com.example.userLogin.domain.User;
import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import com.example.userLogin.jwt.JwtToken;
import com.example.userLogin.jwt.JwtTokenProvider;
import com.example.userLogin.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        try {
            // studentNumber를 String으로 받아와서 사용
            String studentNumber = userLoginRequestDto.getStudentNumber();

            // studentNumber로 사용자 조회
            Optional<User> findByStudentNumber = userRepository.findByStudentNumber(studentNumber);

            // 사용자 존재 여부 확인
            if (findByStudentNumber.isEmpty()) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            User user = findByStudentNumber.get();

            // 비밀번호 비교
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(userLoginRequestDto.getUserPassword(), user.getUserPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getStudentNumber(),
                    userLoginRequestDto.getUserPassword(),
                    Collections.emptyList()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);

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
            throw new RuntimeException("로그인 처리 중 오류가 발생했습니다.");
        }
    }


    @Override
    public void signUp(UserSignupRequestDto userSignupRequestDto, HttpServletResponse response) {

        // 중복 학번 체크
        if (userRepository.findByStudentNumber(userSignupRequestDto.getStudentNumber()).isPresent()) {
            throw new RuntimeException("이미 존재하는 학번입니다.");
        }

        // DTO를 엔티티로 변환하여 저장
        User user = User.builder()
                .studentNumber(userSignupRequestDto.getStudentNumber())
                .userPassword(passwordEncoder.encode(userSignupRequestDto.getUserPassword()))
                .userName(userSignupRequestDto.getUserName())
                .userEmail(userSignupRequestDto.getUserEmail())
                .userPhone(userSignupRequestDto.getUserPhone())
                .build();

        userRepository.save(user);
    }
}
