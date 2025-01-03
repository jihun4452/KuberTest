package com.example.userLogin.service.qrtest;

import com.example.userLogin.entity.Attendance;
import com.example.userLogin.entity.UserEntity;
import com.example.userLogin.repository.AttendanceRepository;
import com.example.userLogin.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final QrCodeService qrCodeService;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 UserRepository userRepository,
                                 QrCodeService qrCodeService) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public String generateAttendanceQrCode(String email) throws Exception {
        // 이메일로 사용자 확인
        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // QR 코드 생성 (sessionId는 email 기반)
        String sessionId = user.getUserEmail(); // 단순히 이메일 사용
        return qrCodeService.generateQrCodeForAttendance(sessionId);
    }

    @Override
    public void markAttendance(String sessionId, String email) {
        // 이메일로 사용자 확인
        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 출석 처리
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        String status = now.isBefore(LocalTime.of(9, 0)) ? "PRESENT" : "LATE";

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setSessionId(sessionId);
        attendance.setDate(today);
        attendance.setTime(now);
        attendance.setStatus(status);

        attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> getAttendanceHistoryByEmail(String email) {
        // 이메일로 사용자 확인 후 출석 기록 반환
        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return attendanceRepository.findByUserId(user.getId());
    }
}

