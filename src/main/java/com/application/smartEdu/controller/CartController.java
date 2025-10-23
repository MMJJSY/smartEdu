package com.application.smartEdu.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.smartEdu.command.CartAddCommand;
import com.application.smartEdu.dto.CartItemWithCourseVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.service.CartService;
import com.application.smartEdu.service.PaymentService;

@Controller
@RequestMapping("/cart")  // ✅ http://localhost/cart
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;

    private Integer getLoginStudentId(HttpSession session) {
        Object obj = session.getAttribute("loginUser");
        if (obj instanceof MemberVO m) {
            return m.getMemberId();
        }
        return null;
    }
    // ====== 찜 목록 페이지 ======
    @GetMapping("")
    public String viewCart(HttpSession session, Model model) throws SQLException {
        Integer studentId = getLoginStudentId(session);
        if (studentId == null) {
            return "redirect:/common/loginForm?retUrl=/cart";
        }

        List<CartItemWithCourseVO> items = cartService.getCartWithCourses(studentId);
        Long total = cartService.sumCartAmountByStudent(studentId);

        model.addAttribute("items", items);
        model.addAttribute("total", total != null ? total : 0L);
        return "cart/cart"; // → templates/cart/cart.html
    }

    // ====== 담기(업서트) ======
        @PostMapping("/add")
        @ResponseBody
        public String addToCart(@ModelAttribute CartAddCommand cmd, HttpSession session) throws SQLException {

            Integer studentId = getLoginStudentId(session);
            if (studentId == null) {
                return "NOT_LOGIN";
            }

            int courseId = cmd.getCourseId();
            Long amount = (cmd.getCourseAmount() == null || cmd.getCourseAmount() < 0) ? 0L : cmd.getCourseAmount();

            cartService.upsertToCart(studentId, courseId, amount);
            return "OK";
        }


    // ====== 단건 삭제 ======
    @DeleteMapping("/remove")
    public String removeFromCart(@RequestParam("courseId") int courseId,
                                 HttpSession session) throws SQLException {

        Integer studentId = getLoginStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login?returnUrl=/cart";
        }

        cartService.removeFromCart(studentId, courseId);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
        public String removeFromCartPost(@RequestParam("courseId") int courseId,
                                        HttpSession session) throws SQLException {
            return removeFromCart(courseId, session);
        }

    @ExceptionHandler(SQLException.class)
    public String handleSqlException(SQLException ex) {
        ex.printStackTrace();
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkoutFromCart(HttpSession session, RedirectAttributes rttr) {
        Integer studentId = getLoginStudentId(session);
        if (studentId == null) return "redirect:/common/loginForm?retUrl=/cart";

        try {
            paymentService.checkoutAllFromCart(studentId);
            rttr.addFlashAttribute("msg", "결제가 완료되었습니다.");
            rttr.addFlashAttribute("msgType", "success");
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", e.getMessage() != null ? e.getMessage() : "결제 처리 중 오류가 발생했습니다.");
            rttr.addFlashAttribute("msgType", "error");
        }
        return "redirect:/cart"; // 카트로 돌아가서 알림 표시
    }

    @PostMapping(value = "/add-ajax", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> addToCartAjax(@ModelAttribute CartAddCommand cmd, HttpSession session) throws SQLException {
        Integer studentId = getLoginStudentId(session);
        if (studentId == null) {
            
            return Map.of(
                "ok", false,
                "needLogin", true,
                "loginUrl", "/common/loginForm?retUrl=" + URLEncoder.encode("/cart", StandardCharsets.UTF_8)
            );
        }
        boolean duplicated = cartService.addToCartAndTellIfDuplicated(studentId, cmd.getCourseId(), cmd.getCourseAmount());
        return Map.of("ok", true, "duplicated", duplicated);
    }



}
//test