package com.application.smartEdu.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.smartEdu.command.PaymentRefundCommand;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.dto.PaymentItemWithCourseVO;
import com.application.smartEdu.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // 공통: 세션에서 로그인 사용자 ID 꺼내기
    private Integer getLoginStudentId(HttpSession session) {
        Object obj = session.getAttribute("loginUser");
        if (obj instanceof MemberVO m) {
            return m.getMemberId();
        }
        return null;
    }

    // 결제 내역 페이지
    @GetMapping
    public String paymentPage(Model model, HttpSession session) throws SQLException {
        Integer studentId = getLoginStudentId(session);
        if (studentId == null) {
            return "redirect:/common/loginForm?retUrl=/payment";
        }
        List<PaymentItemWithCourseVO> items = paymentService.getPaymentsWithCourse(studentId);
        model.addAttribute("items", items);
        return "payment/payment";
    }

    // 환불 (NORMAL -> REFUNDED)
    @PostMapping("/refund")
    public String refund(@ModelAttribute PaymentRefundCommand cmd, HttpSession session) throws SQLException {
        Integer studentId = getLoginStudentId(session);
        if (studentId == null) {
            return "redirect:/common/loginForm?retUrl=/payment";
        }
        // (선택) 본인 결제건인지 검증 로직 추가 권장
        paymentService.refund(cmd.getPaymentId());
        return "redirect:/payment";
    }

    // 카트 전체 결제
    @PostMapping("/checkout")
    public String checkoutFromCart(HttpSession session, RedirectAttributes rttr) throws SQLException {
        Integer studentId = getLoginStudentId(session);
        if (studentId == null) {
            return "redirect:/common/loginForm?retUrl=/cart";
        }
        try {
            paymentService.checkoutAllFromCart(studentId);
            // ✅ 플래시 알림 (카트에서 표시)
            rttr.addFlashAttribute("msg", "결제가 완료되었습니다.");
            rttr.addFlashAttribute("msgType", "success");

            // ✅ 선택 알림용 파라미터 추가 → /cart?paid=1
            rttr.addAttribute("paid", "1");
        } catch (SQLException e) {
            rttr.addFlashAttribute("msg", e.getMessage());
            rttr.addFlashAttribute("msgType", "error");
        }
        return "redirect:/cart";
    }




}
