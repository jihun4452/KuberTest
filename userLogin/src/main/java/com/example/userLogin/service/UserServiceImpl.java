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

        // studentNumber를 통해 UserEntity 조회
        Optional<UserEntity> byStudentNumber = userRepository.findById(Long.valueOf(userLoginRequestDto.getStudentNumber()));

        // 사용자 존재 여부 확인
        if (byStudentNumber.isPresent()) {
            UserEntity userEntity = byStudentNumber.get();

            // 비밀번호 비교
            if (userEntity.getUserPassword().equals(userLoginRequestDto.getUserPassword())) {
                // 비밀번호가 일치하면 로그인 성공
                UserLoginResponseDto responseDto = new UserLoginResponseDto(userEntity);
                return responseDto;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 사용자가 존재하지 않음
            return null;
        }
    }

    @Override
     public void signUp(UserSignupRequestDto userSignupRequestDto)
    {
        UserEntity userEntity = UserEntity.toUserSignUpEntity(userSignupRequestDto);
        userRepository.save(userEntity);
    }

}
