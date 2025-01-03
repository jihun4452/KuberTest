package com.example.userLogin.controller;

import com.example.userLogin.service.qrtest.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // QR 코드 생성 API
    @GetMapping("/generate")
    @Operation(summary = "QR코드 생성")
    public ResponseEntity<String> generateQrCode(@RequestParam String email) {
        try {
            String qrCodePath = attendanceService.generateAttendanceQrCode(email);
            return ResponseEntity.ok("QR 코드가 생성되었습니다. 경로: " + qrCodePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("QR 코드 생성 실패: " + e.getMessage());
        }
    }

    // QR 코드 스캔으로 출석 체크
    @PostMapping("/check")
    @Operation(summary = "QR코드 스캔 출석")
    public ResponseEntity<String> checkAttendance(
            @RequestParam String sessionId,
            @RequestParam String email) {
        try {
            attendanceService.markAttendance(sessionId, email);
            return ResponseEntity.ok("출석이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("출석 처리 실패: " + e.getMessage());
        }
    }

    // 특정 사용자의 출석 기록 조회
    @GetMapping("/{email}/history")
    @Operation(summary = "출석 기록 조회")
    public ResponseEntity<?> getAttendanceHistory(@PathVariable String email) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceHistoryByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("출석 기록 조회 실패: " + e.getMessage());
        }
    }
}
