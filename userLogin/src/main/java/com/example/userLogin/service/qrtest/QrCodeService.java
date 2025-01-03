package com.example.userLogin.service.qrtest;import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import java.io.File;

@Service
public class QrCodeService {

    public String generateQrCodeForAttendance(String sessionId) throws Exception {
        String data = "http://localhost:8080/attendance/check?sessionId=" + sessionId;
        String filePath = "qr_codes/" + sessionId + ".png";

        // 디렉토리 확인 및 생성
        File directory = new File(filePath).getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();  // 디렉토리 생성
        }

        // QR 코드 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 250, 250);

        Path path = Path.of(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return filePath;
    }
}


