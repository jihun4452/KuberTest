package com.example.userLogin.service;

import com.example.userLogin.dto.request.UserLoginRequestDto;
import com.example.userLogin.dto.request.UserSignupRequestDto;
import com.example.userLogin.dto.response.UserLoginResponseDto;
import com.example.userLogin.entity.UserEntity;
import com.example.userLogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

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

            // 로그인 성공
            return new UserLoginResponseDto(userEntity);

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
