package com.application.smartEdu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.application.smartEdu.command.CourseModifyCommand;
import com.application.smartEdu.command.CourseWithLectureDetailVO;
import com.application.smartEdu.command.CoursesRegistCommand;
import com.application.smartEdu.command.LectuerModifyCommand;
import com.application.smartEdu.command.LectureRegistCommand;
import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.LecturesVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.dto.PaymentItemWithCourseVO;
import com.application.smartEdu.enums.CourseApprovedStatus;
import com.application.smartEdu.enums.CourseModifyStatus;
import com.application.smartEdu.enums.CoursePendingStatus;
import com.application.smartEdu.service.CourseService;
import com.application.smartEdu.service.LectureService;
import com.application.smartEdu.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/courses")
public class CourseListController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private PaymentService paymentService;

    @Value("${course.img}")
    private String uploadImgPath;

    @Value("${lecture.video}")
    private String uploadVideoPath;

    /** 공용 강좌 리스트 (ADMIN / INSTRUCTOR / STUDENT) */
    @GetMapping("/list_common")
    public String listCommon(@RequestParam(required = false) String role,
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String filter,
            @ModelAttribute("pageMaker") PageMaker pageMaker,
            HttpSession session,
            Model model) throws Exception {

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if ((role == null || role.isBlank()) && loginUser != null && loginUser.getRole() != null) {
            role = loginUser.getRole().name();
        }
        if (filterType == null || filterType.isBlank())
            filterType = "status";

        // ========== STUDENT ==========
        if ("STUDENT".equalsIgnoreCase(role)) {
            if (loginUser == null)
                return "redirect:/login";
            int studentId = loginUser.getMemberId();

            filterType = "status"; // 고정
            List<PaymentItemWithCourseVO> paid = paymentService.getPaymentsWithCourse(studentId);

            List<Map<String, Object>> courseList = new ArrayList<>();
            for (PaymentItemWithCourseVO it : paid) {
                Map<String, Object> m = new HashMap<>();
                m.put("courseId", it.getCourseId());
                m.put("title", it.getCourseTitle());
                m.put("instructorName", it.getInstructorName());
                m.put("category", it.getCourseCategory());
                m.put("status", null);
                m.put("approvedStatus", null);
                m.put("modifyStatus", null);
                courseList.add(m);
            }

            model.addAttribute("pageTitle", "내 수강 강좌");
            model.addAttribute("pageUrl", "/courses/list_common");
            model.addAttribute("detailUrl", "/courses/detail_common");
            model.addAttribute("role", "STUDENT");
            model.addAttribute("filterType", filterType);
            model.addAttribute("selectedFilter", filter);
            model.addAttribute("filterOptions", List.of());
            model.addAttribute("courseList", courseList);

            pageMaker.setTotalCount(courseList.size());
            model.addAttribute("pageMaker", pageMaker);
            return "courses/list_common";
        }
        // ========== /STUDENT ==========

        // 기본 검색 타입
        switch (filterType) {
            case "approved" -> pageMaker.setSearchType("as");
            case "modify" -> pageMaker.setSearchType("m");
            case "status", "instructor" -> pageMaker.setSearchType("s");
            default -> pageMaker.setSearchType("");
        }
        pageMaker.setKeyword(filter != null ? filter : "");

        Integer instructorId = null;
        if ("INSTRUCTOR".equalsIgnoreCase(role) && loginUser != null) {
            instructorId = loginUser.getMemberId();
        }

        List<CoursesVO> courseList;
        int totalCount;

        if ("status".equals(filterType) && (filter == null || filter.isBlank())) {
            pageMaker.setKeyword("HAS_STATUS");
        }
        if ("modify".equals(filterType) && (filter == null || filter.isBlank())) {
            pageMaker.setKeyword("HAS_MODIFY");
        }

        if (instructorId != null) {
            // ===== 강사 분기 =====
            if ("modify".equalsIgnoreCase(filterType)) {
                // 수정요청 목록: 내 강좌 중 modify_status != null
                List<CoursesVO> allMine = courseService.findByInstructorAndStatus(instructorId, null);
                courseList = allMine.stream()
                        .filter(c -> c.getModifyStatus() != null)
                        .collect(Collectors.toList());

                if (filter != null && !filter.isBlank()) {
                    courseList = courseList.stream()
                            .filter(c -> c.getModifyStatus() != null
                                    && c.getModifyStatus().name().equalsIgnoreCase(filter))
                            .collect(Collectors.toList());
                }

                totalCount = courseList.size();
                pageMaker.setTotalCount(totalCount);
                int start = pageMaker.getStartRow();
                int end = Math.min(start + pageMaker.getPerPageNum(), totalCount);
                courseList = (start < end) ? courseList.subList(start, end) : List.of();

            } else if ("instructor".equalsIgnoreCase(filterType)) {
                // 내강좌 목록: 상태별 필터
                List<CoursesVO> allMine = courseService.findByInstructorAndStatus(instructorId, null);

                if (filter != null && !filter.isBlank()) {
                    allMine = allMine.stream()
                            .filter(c -> c.getStatus() != null
                                    && c.getStatus().name().equalsIgnoreCase(filter))
                            .collect(Collectors.toList());
                }

                totalCount = allMine.size();
                pageMaker.setTotalCount(totalCount);
                int start = pageMaker.getStartRow();
                int end = Math.min(start + pageMaker.getPerPageNum(), totalCount);
                courseList = (start < end) ? allMine.subList(start, end) : List.of();

            } else {
                String statusForDao = "status".equalsIgnoreCase(filterType)
                        ? (filter == null || filter.isBlank() ? null : filter)
                        : null;

                courseList = courseService.findByInstructorAndStatus(instructorId, statusForDao);

                totalCount = courseList.size();
                pageMaker.setTotalCount(totalCount);
                int start = pageMaker.getStartRow();
                int end = Math.min(start + pageMaker.getPerPageNum(), totalCount);
                courseList = (start < end) ? courseList.subList(start, end) : List.of();
            }
        } else {
            // ===== 관리자/원장 분기 =====
            if ("status".equalsIgnoreCase(filterType)) {
                // ✅ 강좌 목록에서는 수정요청 중(REQUEST, PENDING) 강좌 제외
                pageMaker.setExcludeModifyRequests(true);
            }

            courseList = courseService.list(pageMaker);
            totalCount = courseService.countCoursesList(pageMaker);
            pageMaker.setTotalCount(totalCount);
        }

        // 화면 요소
        String pageTitle = "강좌 목록";
        String detailUrl = "/courses/detail_common";
        String pageUrl = "/courses/list_common";

        List<Map<String, String>> filterOptions = new ArrayList<>();
        switch (filterType) {
            case "approved" -> {
                pageTitle = "강좌 승인 요청 목록";
                filterOptions = List.of(
                        Map.of("value", "PENDING", "label", "대기"),
                        Map.of("value", "POSITIVE", "label", "승인"),
                        Map.of("value", "NEGATIVE", "label", "반려"));
            }
            case "modify" -> {
                pageTitle = "강좌 수정 요청 목록";
                filterOptions = List.of(
                        Map.of("value", "REQUEST", "label", "관리자 요청"),
                        Map.of("value", "PENDING", "label", "대기중"),
                        Map.of("value", "ACTIVE", "label", "수정 완료"),
                        Map.of("value", "INACTIVE", "label", "반려됨"));
            }
            case "status" -> {
                pageTitle = "전체 강좌 목록";
                filterOptions = List.of(
                        Map.of("value", "PENDING", "label", "대기"),
                        Map.of("value", "OPEN", "label", "개강"),
                        Map.of("value", "CLOSE", "label", "종강"));
            }
            case "instructor" -> {
                pageTitle = "내 강좌 목록";
                filterOptions = List.of(
                        Map.of("value", "PENDING", "label", "대기"),
                        Map.of("value", "OPEN", "label", "개강"),
                        Map.of("value", "CLOSE", "label", "종강"));
            }
        }

        model.addAttribute("courseList", courseList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("filterOptions", filterOptions);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageUrl", pageUrl);
        model.addAttribute("detailUrl", detailUrl);
        model.addAttribute("selectedFilter", filter);
        model.addAttribute("filterType", filterType);
        model.addAttribute("role", role);

        return "courses/list_common";
    }

    /** 공용 상세보기 */
    @GetMapping("/detail_common")
    public String detailCommon(@RequestParam("courseId") int courseId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String filterType,
            HttpSession session,
            Model model) throws Exception {

        // ✅ 학생이면 접근 권한(결제/수강 여부) 체크 자리 (필요 시 활성화)
        if ("STUDENT".equalsIgnoreCase(role)) {
            MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
            if (loginUser == null)
                return "redirect:/login";
            int studentId = loginUser.getMemberId();
            // TODO: 결제/수강권한 검사 (환불 제외). 실패 시:
            // return "redirect:/courses/list_common?role=STUDENT&error=permission";
        }

        var detailVO = courseService.getAlldetail(courseId);
        model.addAttribute("course", detailVO.getCourse());
        model.addAttribute("lectureList", detailVO.getLectures());
        model.addAttribute("role", role); // STUDENT 전달 시 버튼 자동 숨김(템플릿 조건에 의해)
        model.addAttribute("filterType", filterType);

        return "courses/detail_common";
    }

    /** ✅ 관리자 → 강사 수정 요청 등록 */
    @PostMapping("/requestModify")
    public String requestModify(
            @RequestParam int courseId,
            @RequestParam String comment, // 수정 사유
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String filter) throws Exception {

        CoursesVO course = new CoursesVO();
        course.setCourseId(courseId);

        // 수정 요청 상태로 변경
        course.setModifyStatus(CourseModifyStatus.REQUEST);

        // ✅ 수정 요청 시 강좌 전체 상태를 '대기(PENDING)'로 변경
        course.setStatus(CoursePendingStatus.PENDING);

        // 수정 사유 저장
        course.setModifyComment(comment);

        // DB 반영
        courseService.modify(course);

        String redirectUrl = String.format(
                "redirect:/courses/list_common?role=%s&filterType=%s%s",
                role, filterType,
                (filter != null && !filter.isBlank()) ? "&filter=" + filter : "");

        return redirectUrl;
    }

    /** ✅ 상태 업데이트 (승인 / 수정 / 개강 등) */
    @PostMapping("/updateStatusUnified")
    public String updateStatusUnified(
            @RequestParam int courseId,
            @RequestParam String statusField,
            @RequestParam String statusValue,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String modifyComment,
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String filter) throws Exception {

        CoursesVO course = new CoursesVO();
        course.setCourseId(courseId);

        switch (statusField) {
            case "approvedStatus" -> course.setApprovedStatus(Enum.valueOf(CourseApprovedStatus.class, statusValue));
            case "modifyStatus" -> course.setModifyStatus(Enum.valueOf(CourseModifyStatus.class, statusValue));
            case "status" -> course.setStatus(Enum.valueOf(CoursePendingStatus.class, statusValue));
        }

        if (modifyComment != null && !modifyComment.isBlank()) {
            course.setModifyComment(modifyComment);
        }

        courseService.modify(course);

        String redirectUrl = String.format(
                "redirect:/courses/list_common?role=%s&filterType=%s%s",
                role, filterType,
                (filter != null && !filter.isBlank()) ? "&filter=" + filter : "");
        return redirectUrl;
    }

    // ===========================
    // 강사용 메뉴들
    // ===========================

    // 강좌 생성 화면
    @GetMapping("/registForm")
    public String registCourseForm(HttpSession session, Model model) throws Exception {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        // 로그인 안한 경우 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        // ✅ 로그인한 사용자 정보를 뷰로 전달
        model.addAttribute("loginUser", loginUser);

        return "courses/registForm";
    }

    // ===========================
    // 강좌 등록 + 여러 강의 등록
    // ===========================
    @PostMapping("/regist")
    public ResponseEntity<String> createCourse(@ModelAttribute CoursesRegistCommand command) throws Exception {

        // 1️⃣ 강좌 썸네일 저장
        MultipartFile multi = command.getImg();
        String fileName = savePicture(null, multi);

        // 2️⃣ 강좌 등록
        CoursesVO course = command.toCourseVO();
        course.setImg(fileName);
        courseService.insertCourse(course);
        int courseId = course.getCourseId();

        // 3️⃣ 강의 등록 (여러 강의)
        if (command.getLectures() != null && !command.getLectures().isEmpty()) {
            for (LectureRegistCommand lecCmd : command.getLectures()) {
                LecturesVO lecture = lecCmd.toLectureVO();
                lecture.setCourseId(courseId);

                MultipartFile videoFile = lecCmd.getVideo();
                if (videoFile != null && !videoFile.isEmpty()) {
                    String videoName = saveVideo(null, videoFile);
                    lecture.setVideo(videoName);
                }

                lectureService.addLecture(lecture);
            }
        }

        // ✅ alert + redirect 응답
        String script = """
                <script>
                    alert('강좌 등록이 완료되었습니다.');
                    location.href='/courses/list_common?role=INSTRUCTOR&filterType=instructor';
                </script>
                """;

        return ResponseEntity
                .ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(script);
    }

    // ===========================
    // 파일 저장 메서드
    // ===========================

    public String savePicture(String oldFileName, MultipartFile multi) throws Exception {
        // 새 파일이 없으면 null 반환해서 호출부에서 기존 값 유지하게 함
        if (multi == null || multi.isEmpty())
            return null;

        // 업로드 디렉토리 보장
        File uploadDir = new File(uploadImgPath);
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                throw new IllegalStateException("이미지 업로드 경로 생성 실패: " + uploadImgPath);
            }
        }

        // 기존 파일 삭제
        if (oldFileName != null && !oldFileName.isBlank()) {
            File oldFile = new File(uploadDir, oldFileName);
            if (oldFile.exists()) {
                // 실패하더라도 예외는 던지지 않음
                oldFile.delete();
            }
        }

        // 새 파일명 생성 및 저장
        String newFileName = UUID.randomUUID().toString() + "_" + multi.getOriginalFilename();
        File storeFile = new File(uploadDir, newFileName);
        multi.transferTo(storeFile);

        return newFileName; // DB에는 파일명만 저장
    }

    public String saveVideo(String file, MultipartFile multi) throws Exception {

        String uploadPath = this.uploadVideoPath;

        if (file != null && !file.isEmpty()) {
            File oldFile = new File(uploadPath, file);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        if (multi == null || multi.getSize() == 0) {
            return null;
        }

        // 저장 파일명
        String fileName = null;

        String uuid = UUID.randomUUID().toString().replace("-", "") + ".mp4";
        fileName = uuid + "$$" + multi.getOriginalFilename();

        File storeFile = new File(uploadPath, fileName);
        storeFile.mkdirs();
        multi.transferTo(storeFile);

        return fileName;
    }

    @GetMapping("/getVideoPage")
    public String getVideoPage(@RequestParam("lectureId") Integer lectureId, Model model) throws Exception {
        LecturesVO lecture = lectureService.getLecture(lectureId);
        if (lecture == null || lecture.getVideo() == null) {
            model.addAttribute("error", "해당 강의 영상을 찾을 수 없습니다.");
            return "courses/video_error";
        }

        model.addAttribute("lecture", lecture);
        return "courses/video_player";
    }

    @GetMapping("/getVideo")
    @ResponseBody
    public ResponseEntity<byte[]> getVideo(@RequestParam(name = "lectureId", required = false) Integer lectureId)
            throws Exception {

        LecturesVO lecture = lectureService.getLecture(lectureId);
        if (lecture == null || lecture.getVideo() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        File videoFile = new File(uploadVideoPath, lecture.getVideo());
        if (!videoFile.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try (InputStream in = new FileInputStream(videoFile)) {
            return ResponseEntity.ok()
                    .header("Content-Type", "video/mp4")
                    .body(in.readAllBytes());
        }
    }

    // ===========================
    // 강좌 수정
    // ===========================

    // ===========================
    // ✅ 강좌 수정 화면(GET)
    // ===========================
    @GetMapping("/modifyForm")
    public String modifyForm(@RequestParam int courseId, Model model) throws SQLException {

        // 강좌 + 강의 상세 불러오기
        CourseWithLectureDetailVO detail = courseService.getAlldetail(courseId);

        // 뷰로 데이터 전달
        model.addAttribute("course", detail.getCourse());
        model.addAttribute("lectureList",
                detail.getLectures() != null ? detail.getLectures() : new ArrayList<>());

        return "courses/modifyForm"; // 🔹 course_modify.html 과 동일한 이름으로 지정
    }

    // ===========================
    // ✅ 강좌 수정 처리(POST)
    // ===========================
    @PostMapping(value = "/modify", produces = "text/html; charset=UTF-8")
    @Transactional
    @ResponseBody
    public String modifyCourse(@ModelAttribute CourseModifyCommand command,
            @RequestParam(value = "deletedLectureIds", required = false) String deletedIdsCsv) throws Exception {

        // ... (삭제 처리 등 생략)

        // 1) 강좌 기본정보 업데이트
        CoursesVO course = command.toCourseVO();

        // ⬇⬇⬇ 여기부터 이미지 교체 로직을 넣습니다.
        String oldImg = command.getOldPicture();
        MultipartFile newImg = command.getImg();

        if (newImg != null && !newImg.isEmpty()) {
            String saved = savePicture(oldImg, newImg); // 기존 파일 삭제 + 새 파일 저장(수정한 savePicture 사용)
            course.setImg(saved);
        } else {
            course.setImg(oldImg); // 업로드 안하면 기존 파일 유지
        }
        // ⬆⬆⬆ 여기까지

        course.setModifyStatus(CourseModifyStatus.PENDING);
        courseService.modify(course);
        int courseId = course.getCourseId();

        // 2) 강의 수정/추가 (기존 코드)
        if (command.getLecturesModify() != null && !command.getLecturesModify().isEmpty()) {
            for (LectuerModifyCommand lecCmd : command.getLecturesModify()) {
                LecturesVO lecture = lecCmd.toLectureVO();
                lecture.setCourseId(courseId);

                MultipartFile newVideo = lecCmd.getVideo();
                if (newVideo != null && !newVideo.isEmpty()) {
                    lecture.setVideo(saveVideo(lecCmd.getOldVideo(), newVideo));
                } else {
                    lecture.setVideo(lecCmd.getOldVideo());
                }

                if (lecture.getLectureId() > 0) {
                    lectureService.updateLecture(lecture);
                } else {
                    lectureService.addLecture(lecture);
                }
            }
        }

        return "<script>"
                + "alert('수정 요청이 완료되었습니다.');"
                + "location.href='/courses/list_common?role=INSTRUCTOR&filterType=instructor';"
                + "</script>";
    }

    // ===========================
    // 관리자용 메뉴들
    // ===========================

    // 강좌 상태값 수정
    @PostMapping("/updateStatus")
    @ResponseBody
    public String updateCourseStatus(
            @RequestParam int courseId,
            @RequestParam(required = false) String approvedStatus,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String modifyStatus) throws Exception {
        CoursesVO vo = new CoursesVO();
        vo.setCourseId(courseId);
        if (approvedStatus != null && !"null".equals(approvedStatus)) {
            vo.setApprovedStatus(Enum.valueOf(CourseApprovedStatus.class, approvedStatus));
        }
        if (status != null && !"null".equals(status)) {
            vo.setStatus(Enum.valueOf(CoursePendingStatus.class, status));
        }
        if (modifyStatus != null && !"null".equals(modifyStatus)) {
            vo.setModifyStatus(Enum.valueOf(CourseModifyStatus.class, modifyStatus));
        }

        courseService.modify(vo);
        return "<script>"
                + "alert('승인 상태가 변경되었습니다.');"
                + "location.href='/courses/status_list';"
                + "</script>";
    }

    @GetMapping("/status_list_old")
    public String courseList(PageMaker pageMaker, Model model) throws Exception {
        // pageMaker.searchType = "status" 일 때 pageMaker.keyword에 상태값이 들어감
        String url = "courses/courseList";
        List<CoursesVO> courses = courseService.list(pageMaker);

        model.addAttribute("courses", courses);
        model.addAttribute("pageMaker", pageMaker);

        return url; // courseList.html
    }

    @GetMapping("/status_list")
    public String courseStatusList(
            @ModelAttribute("pageMaker") PageMaker pageMaker,
            @RequestParam(required = false) String filter,
            Model model) throws Exception {

        String url = "courses/status_list";

        // 🔹 검색 조건 지정
        pageMaker.setSearchType("as"); // approvedStatus 기준 검색

        if (filter != null && !filter.isEmpty()) {
            pageMaker.setKeyword(filter); // 예: "PENDING", "POSITIVE", "NEGATIVE"
        } else {
            pageMaker.setKeyword(""); // 전체 보기
        }

        List<CoursesVO> courseList = courseService.list(pageMaker);

        model.addAttribute("courseList", courseList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("selectedFilter", filter);

        return url;
    }

    // 상세보기 (관리자용)
    @GetMapping("/status_detail")
    public String courseStatusDetail(@RequestParam int courseId, Model model) throws Exception {
        CourseWithLectureDetailVO detail = courseService.getAlldetail(courseId);
        model.addAttribute("course", detail.getCourse());
        model.addAttribute("lectureList", detail.getLectures());
        return "courses/status_detail";
    }

    // 승인 / 거절 처리
    @PostMapping("/updateApprovedStatus")
    @ResponseBody
    public String updateApprovedStatus(@RequestParam int courseId, @RequestParam String approvedStatus)
            throws Exception {
        CoursesVO vo = new CoursesVO();
        vo.setCourseId(courseId);
        vo.setApprovedStatus(Enum.valueOf(CourseApprovedStatus.class, approvedStatus));
        courseService.modify(vo);
        return "success";
    }

    // 관리자 수정요청 리스트 + 세부사항 / 버튼으로 상태 값 변경

    @GetMapping("/modify_list")
    public String courseModifyList(
            @ModelAttribute("pageMaker") PageMaker pageMaker,
            @RequestParam(required = false) String filter,
            Model model) throws Exception {

        pageMaker.setSearchType("m");
        pageMaker.setKeyword(filter != null ? filter : "");

        List<CoursesVO> courseList = courseService.list(pageMaker)
                .stream()
                .filter(c -> c.getModifyStatus() != null) // ✅ null 값 제거
                .collect(Collectors.toList());
        pageMaker.setTotalCount(courseList.size()); // ✅ 필터링 후 개수 재설정

        model.addAttribute("courseList", courseList);
        model.addAttribute("selectedFilter", filter);
        model.addAttribute("pageMaker", pageMaker);

        return "courses/modify_list";
    }

    @GetMapping("/modify_detail")
    public String courseModifyDetail(@RequestParam int courseId, Model model) throws Exception {
        CourseWithLectureDetailVO detail = courseService.getAlldetail(courseId);
        model.addAttribute("course", detail.getCourse());
        model.addAttribute("lectureList", detail.getLectures());
        return "courses/modify_detail";
    }

}
