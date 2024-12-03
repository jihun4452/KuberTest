package com.example.userLogin.service;

import com.example.userLogin.domain.User;
import com.example.userLogin.dto.user.request.UserLoginRequestDto;
import com.example.userLogin.dto.user.request.UserSignupRequestDto;
import com.example.userLogin.dto.user.response.UserLoginResponseDto;
import com.example.userLogin.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    //나중에 에러 코드 추가 지금은 런타임
    private User findByStudentNumberOrThrow(String studentNumber) {
        return userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new RuntimeException("해당 학번은 없습니다."));
    }

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
