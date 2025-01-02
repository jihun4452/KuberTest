package com.example.userLogin.controller;

import com.example.userLogin.dto.request.ScheduleRequestDto;
import com.example.userLogin.dto.response.MainScheduleResponseDto;
import com.example.userLogin.dto.response.ScheduleResponseDto;
import com.example.userLogin.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleApiController {

    private final ScheduleService scheduleService;

    @Operation(summary = "일정 생성")
    @PostMapping("/create")
    public ResponseEntity<String> createSchedule(@RequestBody ScheduleRequestDto request) {
        scheduleService.createSchedule(request);
        return ResponseEntity.ok("일정이 추가되었습니다.");
    }

    @Operation(summary = "캘린더 전체 조회(월 단위)")
    @GetMapping("/{yeqr}/{month}")
    public ResponseEntity<Map<LocalDate, List<MainScheduleResponseDto>>> findSchedulesForMonth(@PathVariable int yeqr, @PathVariable int month) {
        return ResponseEntity.ok(scheduleService.getSchedulesForMonth(yeqr, month));
    }

    @Operation(summary = "캘린더 일정 조회(일 단위)")
    @GetMapping("/{date}")
    public ResponseEntity<List<ScheduleResponseDto>> findSchedule(@PathVariable LocalDate date) {
        return ResponseEntity.ok(scheduleService.getSchedules(date));
    }

    @Operation(summary = "캘린더 일정 수정")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto request) {
        scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok("수정 완료!");
    }

    @Operation(summary = "캘린더 일정 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("삭제 완료!");
    }


}
