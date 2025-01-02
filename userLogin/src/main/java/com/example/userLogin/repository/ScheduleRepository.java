package com.example.userLogin.repository;

import com.example.userLogin.dto.response.MainScheduleResponseDto;
import com.example.userLogin.dto.response.ScheduleResponseDto;
import com.example.userLogin.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
  List<ScheduleEntity> findByStartDateOrderByStartTimeAsc(LocalDate startDate);
  List<ScheduleEntity> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
