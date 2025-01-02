package com.example.userLogin.dto.request;

import com.example.userLogin.entity.ScheduleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduleRequestDto {

    @Schema(description = "일정 제목")
    private String title;
    @Schema(description = "일정 내용")
    private String contents;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "시작 날짜", example = "2025-01-03")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "종료 날짜", example = "2025-01-03")
    private LocalDate endDate;
    @DateTimeFormat(pattern = "HH:mm")
    @Schema(description = "시작 시간 (HH:mm 형식)", example = "14:30")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    @Schema(description = "종료 시간 (HH:mm 형식)", example = "14:30")
    private LocalTime endTime;

    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
            .title(title)
            .content(contents)
            .startDate(startDate)
            .endDate(endDate)
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }
}
