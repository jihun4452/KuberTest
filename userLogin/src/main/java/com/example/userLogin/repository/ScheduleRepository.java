package com.example.userLogin.repository;

import com.example.userLogin.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

}
