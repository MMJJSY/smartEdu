package com.application.smartEdu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import com.application.smartEdu.enums.CourseApprovedStatus;
import com.application.smartEdu.enums.CourseModifyStatus;
import com.application.smartEdu.enums.CoursePendingStatus;
import com.application.smartEdu.service.CourseService;
import com.application.smartEdu.service.LectureService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private LectureService lectureService;

    @Value("${course.img}")
    private String uploadImgPath;

    @Value("${lecture.video}")
    private String uploadVideoPath;

    @GetMapping("/list")
    public String courseList(@RequestParam(value = "category", required = false) String category,
            @ModelAttribute("pageMaker") PageMaker pageMaker,
            Model model) throws Exception {
        String url = "courses/list";

        // ✅ 항상 OPEN만 보이게
        pageMaker.setStatusFilter("OPEN");

        // ✅ 카테고리도 필터로 (searchType/keyword 쓰지 말고)
        if (category != null && !category.isEmpty()) {
            pageMaker.setCategoryFilter(category);
            // pageMaker.setSearchType(null); // 혹시 이전 값 남아있다면 정리
            // pageMaker.setKeyword(null);
        } else {
            pageMaker.setCategoryFilter(null);
        }

        List<CoursesVO> courseList = courseService.list(pageMaker);

        model.addAttribute("courseList", courseList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("selectedCategory", category);
        return url;
    }

    @GetMapping("/detail")
    public String CourseWithLectureDetail(@RequestParam Integer courseId, Model model, HttpServletRequest request)
            throws Exception {

        String url = "courses/detail";

        ServletContext application = request.getServletContext();
        HttpSession session = request.getSession(false); // <- false로 바꿔서 세션이 없으면 새로 만들지 않게
        MemberVO loginUser = null;

        if (session != null) {
            loginUser = (MemberVO) session.getAttribute("loginUser");
        }

        // 로그인 여부 상관없이 강좌 정보는 보여줌
        CourseWithLectureDetailVO course = courseService.getAlldetail(courseId);

        if (loginUser != null) {
            String key = "course:" + loginUser.getEmail() + courseId;
            if (application.getAttribute(key) == null) {
                application.setAttribute(key, "");
            }
        }

        model.addAttribute("course", course.getCourse());
        model.addAttribute("lectureList", course.getLectures() != null ? course.getLectures() : new ArrayList<>());
        model.addAttribute("loginUser", loginUser); // 로그인 여부 템플릿에서 확인하기 위해 추가

        return url;
    }

}
