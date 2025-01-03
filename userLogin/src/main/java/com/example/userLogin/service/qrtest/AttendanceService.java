package com.example.userLogin.service.qrtest;

import com.example.userLogin.entity.Attendance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {
    String generateAttendanceQrCode(String email) throws Exception;
    void markAttendance(String sessionId, String email);
    List<Attendance> getAttendanceHistoryByEmail(String email);
}
