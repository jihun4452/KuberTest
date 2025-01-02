package com.example.userLogin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainScheduleResponseDto {
  private Long id;
  private LocalDate startDate;
  private String title;
}
