package com.application.smartEdu.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.application.smartEdu.dao.ReviewDAO;
import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.dto.ReviewVO;
import com.application.smartEdu.enums.Role;
import com.application.smartEdu.service.QuestionService;
import com.application.smartEdu.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private QuestionService questionService;

    @GetMapping
    public String reviewList(@RequestParam(required = false, defaultValue = "전체") String category,
            HttpSession session, Model model) throws SQLException {

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        List<ReviewVO> reviewList = reviewService.getReviewList(category);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("selectedCategory", category);

        if (loginUser != null) {
            List<CoursesVO> myCourses = questionService.getCourseBystudent(loginUser.getMemberId());
            model.addAttribute("myCourses", myCourses);
            model.addAttribute("currentUserId", loginUser.getMemberId());
            model.addAttribute("currentUserRole", loginUser.getRole());
        } else {
            model.addAttribute("myCourses", null);
            model.addAttribute("currentUserId", null);
        }

        return "review/student-review";
    }

    @PostMapping("/regist")
    @ResponseBody
    public String insertReview(@RequestBody ReviewVO review, HttpSession session) throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if(loginUser == null) {
            return "unauthorized";
        }

        review.setStudentId(loginUser.getMemberId());

        ReviewVO existing = reviewService.getReviewByStudentAndCourse(review.getStudentId(), review.getCourseId());
        if (existing != null) {
            return "duplicate";
        }

        reviewService.insertReview(review);
        return "success";
    }

    @PostMapping("/delete/{reviewId}")
    @ResponseBody
    public String deleteReview(@PathVariable int reviewId, HttpSession session) throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "unauthorized";
        }

        ReviewVO review = reviewService.getReviewById(reviewId);
        if (review == null ) {
            return "not_found";
        }

        if (review.getStudentId() == loginUser.getMemberId() || Role.ADMIN == loginUser.getRole()) {
            reviewService.deleteReview(reviewId);
            return "deleted";
         } else {
            return "forbidden";
         }

    }

    @GetMapping("/filter")
    @ResponseBody
    public List<ReviewVO> filterReview(@RequestParam(required = false) String category) throws SQLException {
        if (category == null || category.equals("전체")) {
            return reviewService.getReviewList(null);
        } else {
            return reviewService.getReviewList(category);
        }
    }

}
