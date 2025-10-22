package com.application.smartEdu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.InstructorVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.dto.PaymentItemWithCourseVO;
import com.application.smartEdu.dto.QuestionVO;
import com.application.smartEdu.service.CourseService;
import com.application.smartEdu.service.MemberService;
import com.application.smartEdu.service.PaymentService;
import com.application.smartEdu.service.QuestionService;

@Controller
public class DashboardController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/dashboard")
    public String dashboardMain(HttpSession session, Model model) {
        // 1️⃣ 세션에서 로그인 유저 정보 가져오기
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        // 2️⃣ 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        // 4️⃣ role 값 ENUM → String 변환
        String role = loginUser.getRole().name(); // "INSTRUCTOR"
        String roleName = loginUser.getRole().getRoleName(); // "강사"

        model.addAttribute("role", role);
        model.addAttribute("roleName", roleName);
        // 5️⃣ 메인 대시보드 뷰 반환
        return "dashboard/dashboard";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(
            @RequestParam(value = "instPage", required = false, defaultValue = "1") int instPage,
            @RequestParam(value = "instKeyword", required = false) String instKeyword,
            Model model) throws Exception {

        // ✅ 강좌(승인 대기)
        PageMaker coursePm = new PageMaker();
        coursePm.setSearchType("as"); // 승인요청 전용
        coursePm.setKeyword("PENDING");
        List<?> pendingCourses = courseService.list(coursePm);

        // ✅ 강사(승인 대기) - 이름검색 + 대기상태 필터
        PageMaker instPm = new PageMaker();
        instPm.setPage(instPage);
        instPm.setPerPageNum(8);
        instPm.setSearchType("n"); // 이름검색
        instPm.setKeyword(instKeyword == null ? "" : instKeyword.trim());

        List<InstructorVO> instructors = memberService.findPendingInstructors(instPm);

        // 🔴 여기서 '대기'만 남기기 (서비스/DAO가 전부 줘도 대시보드에서는 확실히 필터)
        List<InstructorVO> pendingInstructors = instructors.stream()
                .filter(iv -> iv.getPendingStatus() != null
                        && "PENDING".equalsIgnoreCase(iv.getPendingStatus().name()))
                .toList();

        model.addAttribute("pendingCourses", pendingCourses);
        model.addAttribute("pendingInstructors", pendingInstructors);
        model.addAttribute("instPm", instPm);
        model.addAttribute("coursePm", coursePm);

        return "dashboard/admin";
    }

    @GetMapping("/dashboard/instructor")
    public String instructorDashboard(HttpSession session, Model model) throws Exception {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null)
            return "redirect:/common/loginForm";

        PageMaker pageMaker = new PageMaker();
        pageMaker.setSearchType("i"); // instructor 기준 검색
        pageMaker.setKeyword(loginUser.getName());

        List<CoursesVO> courseList = courseService.list(pageMaker);

        model.addAttribute("courseList", courseList);
        model.addAttribute("pageMaker", pageMaker);

        String role = loginUser.getRole().name();
        int memberId = loginUser.getMemberId();

        List<QuestionVO> questionList = questionService.getQuestionList(role, memberId);
        model.addAttribute("questionList", questionList);
        return "dashboard/instructor";
    }

    /** ✅ 학생 마이페이지: 결제한 강좌 목록 (list_common 학생 분기와 동일한 방식) */
    @GetMapping("/dashboard/student")
    public String studentDashboard(@RequestParam(value = "cat", required = false) String cat,
            HttpSession session,
            Model model) throws Exception {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null)
            return "redirect:/common/loginForm";

        // 결제 + 강좌 JOIN (REFUNDED 제외)
        List<PaymentItemWithCourseVO> paid = paymentService.getPaymentsWithCourse(loginUser.getMemberId());

        // 필터/목록 준비
        List<Map<String, Object>> myCourses = new ArrayList<>();
        Set<String> categories = new TreeSet<>();

        String selected = (cat == null) ? "" : cat.trim();
        boolean hasFilter = !selected.isEmpty();

        for (PaymentItemWithCourseVO p : paid) {
            // enum이면 name(), 클래스면 toString()/getLabel()로 문자열화
            String category = (p.getCourseCategory() == null) ? null : p.getCourseCategory().toString();
            if (category != null && !category.trim().isEmpty()) {
                categories.add(category);
            }

            // 카테고리 필터 적용
            if (hasFilter && (category == null || !category.equals(selected)))
                continue;

            Map<String, Object> row = new HashMap<>();
            row.put("courseId", p.getCourseId());
            row.put("title", p.getCourseTitle());
            row.put("category", category);
            row.put("instructorName", p.getInstructorName());
            row.put("remainingDays", null); // null = 무제한
            myCourses.add(row);
        }

        model.addAttribute("role", "STUDENT");
        model.addAttribute("myCourses", myCourses);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCat", selected);

        return "dashboard/student";
    }

    // @GetMapping("/admin")
    // public String adminDashboard(Model model) {
    // model.addAttribute("pendingInstructors", instructorService.getPendingList());
    // model.addAttribute("pendingCourses", courseService.getPendingCourses());
    // return "dashboard/admin"; // admin.html
    // }
}
