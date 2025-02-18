//package com.springboot.board.service;
//
//import com.springboot.exception.BusinessLogicException;
//import com.springboot.exception.ExceptionCode;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//25.02.18 15:18 잘못 구현된 것으로 판단되어 사용을 금지 합니다. 최용준 (인)
//@Service
//public class FileUploadService {
//    private static final String UPLOAD_DIR = "C:/practice/soloproject_choiyongjun/src/main/resources/uploads/";
//
//    public String storeFile(MultipartFile file) {
//        try {
//            // 파일 확장자 검증
//            validateFileType(file);
//
//            // 저장 경로 설정
//            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            Path filePath = Paths.get(UPLOAD_DIR + fileName);
//
//            // 디렉토리가 없으면 생성
//            Files.createDirectories(filePath.getParent());
//
//            // 파일 저장
//            Files.write(filePath, file.getBytes());
//
//            return filePath.toString();  // 저장된 파일 경로 반환
//        } catch (IOException e) {
//            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
//        }
//    }
//
//    private void validateFileType(MultipartFile file) {
//        String contentType = file.getContentType();
//        if (contentType == null ||
//                !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"))) {
//            throw new RuntimeException("허용되지 않은 파일 형식입니다. (JPEG, PNG, GIF만 가능)");
//        }
//    }
//}
