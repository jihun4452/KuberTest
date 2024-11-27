package com.example.userLogin.service;

import com.example.userLogin.dto.request.AddScheduleRequestDto;
import com.example.userLogin.entity.ScheduleEntity;
import com.example.userLogin.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 스케줄 글 추가 메서드
    public ScheduleEntity save(AddScheduleRequestDto request)   {
        return scheduleRepository.save(request.toEntity());
    }

    public List<ScheduleEntity> findAll() {
        return scheduleRepository.findAll();
    }
}
