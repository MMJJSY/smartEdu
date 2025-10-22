package com.application.smartEdu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

                // ✅ 강좌 이미지 업로드 경로
                // 실제 파일 경로: C:/course/img/upload/
                // 접근 URL 예시: http://localhost:8080/upload/course/파일명.jpg
                registry.addResourceHandler("/upload/course/**")
                                .addResourceLocations("file:///C:/course/img/upload/");

                // ✅ 강의 동영상 업로드 경로
                // 실제 파일 경로: C:/lecture/video/upload/
                // 접근 URL 예시: http://localhost:8080/upload/lecture/video/파일명.mp4
                registry.addResourceHandler("/upload/lecture/video/**")
                                .addResourceLocations("file:///C:/lecture/video/upload/");

                // ✅ Summernote 이미지 (선택)
                // 실제 파일 경로: C:/summernote/img/
                // 접근 URL 예시: http://localhost:8080/upload/summernote/img/파일명.png
                registry.addResourceHandler("/upload/summernote/img/**")
                                .addResourceLocations("file:///C:/summernote/img/");

                registry.addResourceHandler("/upload/resume/**")
                                .addResourceLocations("file:///C:/member/resume/upload/");

        }
}
