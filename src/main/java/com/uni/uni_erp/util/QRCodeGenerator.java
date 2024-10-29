package com.uni.uni_erp.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
//    public static void main(String[] args) {
//        String url = "https://www.naver.com";  // 매핑할 URL
//        int width = 350;
//        int height = 350;
//        String filePath = "C:\\Users\\seal0\\QRCode.png";  // 저장할 파일 경로
//
//        try {
//            generateQRCodeImage(url, width, height, filePath);
//            System.out.println("QR 코드가 생성되었습니다: " + filePath);
//        } catch (WriterException | IOException e) {
//            System.err.println("QR 코드 생성 중 오류 발생: " + e.getMessage());
//        }
//    }
}
