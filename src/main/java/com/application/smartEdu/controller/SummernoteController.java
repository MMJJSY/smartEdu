package com.application.smartEdu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/summernote")
public class SummernoteController {

    @Value("${summernote.img}")
    private String imgPath;

    @PostMapping("/uploadImg")
    public ResponseEntity<String> uploadImg(MultipartFile file, HttpServletRequest request) throws IOException {
        String savePath = imgPath.replace("/", File.separator);
        File dir = new File(savePath);
        if (!dir.exists())
            dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalName;

        File saveFile = new File(dir, fileName);
        file.transferTo(saveFile);

        String imgUrl = request.getContextPath() + "/summernote/getImg?fileName=" + fileName;
        return new ResponseEntity<>(imgUrl, HttpStatus.OK);
    }

    @GetMapping("/getImg")
    public ResponseEntity<byte[]> getImg(String fileName) throws Exception {
        String savePath = imgPath.replace("/", File.separator);
        File sendFile = new File(savePath, fileName);

        try (InputStream in = new FileInputStream(sendFile)) {
            String contentType = null;

            // 확장자에 따라 Content-Type 설정
            String lowerName = fileName.toLowerCase();
            if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (lowerName.endsWith(".png")) {
                contentType = "image/png";
            } else if (lowerName.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (lowerName.endsWith(".webp")) {
                contentType = "image/webp";
            } else {
                contentType = "application/octet-stream"; // 기타 파일
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", contentType);

            return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
        }
    }

    @GetMapping("/deleteImg")
    public ResponseEntity<String> deleteImg(String fileName) {
        String savePath = imgPath.replace("/", File.separator);
        File target = new File(savePath, fileName);

        if (target.exists()) {
            target.delete();
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("파일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

}