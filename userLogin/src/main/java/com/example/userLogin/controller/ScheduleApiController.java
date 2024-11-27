package com.example.userLogin.controller;

import com.example.userLogin.dto.request.AddScheduleRequestDto;
import com.example.userLogin.dto.response.ScheduleResponseDto;
import com.example.userLogin.entity.ScheduleEntity;
import com.example.userLogin.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ScheduleApiController {

    private final ScheduleService scheduleService;

    //HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/schedule")
    //@RequestBody 로 요청 본문 값 매핑
    public ResponseEntity<ScheduleEntity> addSchedule(@RequestBody AddScheduleRequestDto request) {
        ScheduleEntity savedSchedule = scheduleService.save(request);

    //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedSchedule);
    }

    @GetMapping("/api/schedule")
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedule() {
        List<ScheduleResponseDto> schedule = scheduleService.findAll()
                .stream()
                .map(ScheduleResponseDto::new)
                .toList();

        return ResponseEntity.ok()
                .body(schedule);

    }
}
