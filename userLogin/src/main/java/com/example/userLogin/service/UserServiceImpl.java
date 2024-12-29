package com.example.userLogin.service;

import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import com.example.userLogin.entity.UserEntity;
import com.example.userLogin.jwt.JwtToken;
import com.example.userLogin.jwt.JwtTokenProvider;
import com.example.userLogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        try {
            // studentNumber를 Long으로 변환
            Long studentNumber = Long.valueOf(userLoginRequestDto.getStudentNumber());

            // studentNumber로 사용자 조회
            Optional<UserEntity> findByStudentNumber = userRepository.findById(studentNumber);

            // 사용자 존재 여부 확인
            if (findByStudentNumber.isEmpty()) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            UserEntity userEntity = findByStudentNumber.get();

            // 비밀번호 비교
            if (!userEntity.getUserPassword().equals(userLoginRequestDto.getUserPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

            // 로그인 성공 시, JWT 토큰 발급
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userLoginRequestDto.getStudentNumber(), userLoginRequestDto.getUserPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            JwtToken jwtToken = jwtTokenProvider.issueToken(authentication);

            // 토큰과 사용자 정보 반환
            return UserLoginResponseDto.builder()
                    .studentNumber(Long.valueOf(userEntity.getStudentNumber()))
                    .userName(userEntity.getUserName())
                    .accessToken(jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .build();

        } catch (NumberFormatException e) {
            throw new RuntimeException("학번 형식이 잘못되었습니다.");
        }
    }

    @Override
     public void signUp(UserSignupRequestDto userSignupRequestDto)
    {
        UserEntity userEntity = UserEntity.toUserSignUpEntity(userSignupRequestDto);
        userRepository.save(userEntity);
    }

}
