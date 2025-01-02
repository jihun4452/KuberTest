package com.example.userLogin.service;

import com.example.userLogin.dto.request.ScheduleRequestDto;
import com.example.userLogin.dto.response.MainScheduleResponseDto;
import com.example.userLogin.dto.response.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
  // 일정 생성
  void createSchedule(ScheduleRequestDto requestDto);
  // 일정 조회(월 단위)
  Map<LocalDate, List<MainScheduleResponseDto>> getSchedulesForMonth(int year, int month);
  // 일정 조회(일 단위 시작시간 순으로 정렬해서 보여주기)
  List<ScheduleResponseDto> getSchedules(LocalDate date);
  // 일정 수정
  void updateSchedule(Long id, ScheduleRequestDto requestDto);
  // 일정 삭제
  void deleteSchedule(Long id);
}
