package com.example.userLogin.repository;


import com.example.userLogin.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByStudentNumber(String studentNumber);
    Optional<UserEntity> findByUserEmail(String userEmail);
    Optional<UserEntity> findById(Long id);
}
