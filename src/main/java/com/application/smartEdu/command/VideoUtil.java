package com.application.smartEdu.command;

import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.javacpp.PointerPointer;

public class VideoUtil {

    /**
     * 동영상 길이를 분 단위로 반환
     * @param videoPath 동영상 파일 경로
     * @return 길이(분)
     * @throws Exception 파일 열기나 정보 추출 실패 시
     */
    public static double getVideoDurationInMinutes(String videoPath) throws Exception {
        // FFmpeg 초기화
        avformat.av_register_all();

        AVFormatContext formatContext = avformat.avformat_alloc_context();

        // 파일 열기
        if (avformat.avformat_open_input(formatContext, videoPath, null, null) != 0) {
            throw new Exception("동영상 파일을 열 수 없습니다: " + videoPath);
        }

        // 스트림 정보 읽기
        if (avformat.avformat_find_stream_info(formatContext, (PointerPointer) null) < 0) {
            throw new Exception("동영상 스트림 정보를 읽을 수 없습니다: " + videoPath);
        }

        // 길이 계산
        long duration = formatContext.duration(); // 마이크로초 단위
        double minutes = duration / 1000000.0 / 60.0;

        // 리소스 해제
        avformat.avformat_close_input(formatContext);

        return minutes;
    }

    public static void main(String[] args) {
        try {
            String path = "C:\\SmartEdu\\uploads\\videos\\sample.mp4";
            double duration = getVideoDurationInMinutes(path);
            System.out.println("영상 길이: " + duration + "분");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
