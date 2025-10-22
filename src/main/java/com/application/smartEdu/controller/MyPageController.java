package com.application.smartEdu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.exception.NotFoundEmailException;
import com.application.smartEdu.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MemberService memberService;

    /**
     * 권한별 대시보드 분기
     */
    @GetMapping
    public String mypageDashboard(HttpSession session, Model model) {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        // 로그인 안 된 경우 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        // role에 따라 다른 대시보드 리턴
        String role = loginUser.getRole().toString(); // STUDENT / INSTRUCTOR / ADMIN
        model.addAttribute("member", loginUser);

        switch (role) {
            case "ADMIN":
                return "mypage/admin_mypage";
            case "INSTRUCTOR":
                return "mypage/instructor_mypage";
            default:
                return "mypage/student_mypage";
        }
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/info")
    public String studentInfo(HttpSession session, Model model) {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        model.addAttribute("member", loginUser);

        // 권한별로 다른 정보 페이지
        String role = loginUser.getRole().toString();
        switch (role) {
            case "ADMIN":
                return "mypage/admin_info";
            case "INSTRUCTOR":
                return "mypage/student_info";
            default:
                return "mypage/student_info";
        }
    }

    /**
     * 비밀번호 확인
     */
    @GetMapping("/pwdcheck")
    public String pwdCheck(HttpSession session, Model model) {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        model.addAttribute("member", loginUser);
        return "mypage/student_pwdcheck"; // 비밀번호 확인은 공통 가능
    }

    @PostMapping("/pwdcheck")
    public String pwdCheck(String pwd, HttpSession session, RedirectAttributes rttr) throws Exception {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        MemberVO member = memberService.getMember(loginUser.getEmail());

        if (member != null && member.getPwd().equals(pwd)) {
            return "redirect:/mypage/pwdedit";
        } else {
            rttr.addFlashAttribute("msg", "비밀번호가 올바르지 않습니다.");
            return "redirect:/mypage/pwdcheck";
        }
    }

    /**
     * 비밀번호 수정
     */
    @GetMapping("/pwdedit")
    public String pwdEditForm(HttpSession session, Model model) {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }
        model.addAttribute("member", loginUser);
        return "mypage/student_pwdedit"; // 공통 가능
    }

    @PostMapping("/pwdedit")
    public String pwdEdit(String newPwd, String confirmPwd,
            HttpSession session, RedirectAttributes rttr) throws Exception {

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        if (!newPwd.equals(confirmPwd)) {
            rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/pwdedit";
        }

        try {
            memberService.changePassword(loginUser.getEmail(), newPwd);
            loginUser.setPwd(newPwd);
            rttr.addFlashAttribute("msg", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/mypage/pwdcheck";
        } catch (NotFoundEmailException e) {
            rttr.addFlashAttribute("msg", "회원 정보를 찾을 수 없습니다.");
            return "redirect:/common/loginForm";
        }
    }

    /**
     * 내 강좌 페이지
     */
    @GetMapping("/mycourse")
    public String mycourse(HttpSession session, Model model) {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        String role = loginUser.getRole().toString();

        switch (role) {
            case "INSTRUCTOR":
                return "mypage/instructor_course";
            case "STUDENT":
                return "mypage/student_class_detail";
            default:
                return "redirect:/mypage"; // 관리자는 해당 없음
        }
    }
    /* ================================
       ✅ 레거시/임시 경로 리다이렉트 (URL만 추가)
       ================================ */

    // "/mypage/favorites", "/mypage/wishlist", "/mypage/wish" → /cart
    @GetMapping({"/favorites", "/wishlist", "/wish"})
    public String redirectWishlistToCart() {
        return "redirect:/cart";
    }

    // "/mypage/payments", "/mypage/payment-history" → /payment
    @GetMapping({"/payments", "/payment-history"})
    public String redirectPaymentsToPayment() {
        return "redirect:/payment";
    }

    // 필요 시: "/mypage/wishlist/**" 하위까지 모두 /cart 로 보낼 때
    // @GetMapping("/wishlist/**")
    // public String redirectWishlistAnyToCart() { return "redirect:/cart"; }

}
