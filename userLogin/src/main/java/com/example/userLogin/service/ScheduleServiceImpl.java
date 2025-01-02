package com.example.userLogin.service;

import com.example.userLogin.dto.request.ScheduleRequestDto;
import com.example.userLogin.dto.response.MainScheduleResponseDto;
import com.example.userLogin.dto.response.ScheduleResponseDto;
import com.example.userLogin.entity.ScheduleEntity;
import com.example.userLogin.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public void createSchedule(ScheduleRequestDto requestDto) {
        ScheduleEntity schedule = requestDto.toEntity();
        scheduleRepository.save(schedule);
    }

    @Override
    public Map<LocalDate, List<MainScheduleResponseDto>> getSchedulesForMonth(int year, int month) {
        LocalDate startDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        List<ScheduleEntity> schedules =  scheduleRepository.findByStartDateBetween(startDate, endDate);

      return schedules.stream()
            .map(schedule -> MainScheduleResponseDto.builder()
                .id(schedule.getId())
                .startDate(schedule.getStartDate())
                .title(schedule.getTitle())
                .build())
            .collect(Collectors.groupingBy(
                MainScheduleResponseDto::getStartDate,
                Collectors.toList()
            ));
    }


    @Override
    public List<ScheduleResponseDto> getSchedules(LocalDate startDate) {
        // 일 단위 일정 조회
        List<ScheduleEntity> schedules = scheduleRepository.findByStartDateOrderByStartTimeAsc(startDate);
        if (schedules.isEmpty()) {
            throw new NotFoundException("일정이 없습니다!");
        }
      return schedules.stream()
            .map(schedule -> ScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .contents(schedule.getContents())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build())
            .toList();
    }

    @Override
    public void updateSchedule(Long id, ScheduleRequestDto requestDto) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("찾을 수 없는 일정입니다."));

        schedule.updateSchedule(requestDto);
    }

    @Override
    public void deleteSchedule(Long id) {
        if(!scheduleRepository.existsById(id))
            throw new NotFoundException("찾을 수 없는 일정입니다.");
        scheduleRepository.deleteById(id);
    }
}
