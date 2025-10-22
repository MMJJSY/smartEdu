package com.application.smartEdu.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.smartEdu.dto.CommentVO;
import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.dto.QuestionVO;
import com.application.smartEdu.service.CommentService;
import com.application.smartEdu.service.QuestionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public String QuestionList(Model model, HttpSession session) throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        String role = loginUser.getRole().name();
        int memberId = loginUser.getMemberId();

        List<QuestionVO> questionList = questionService.getQuestionList(role, memberId);
        model.addAttribute("questionList", questionList);

        return "question/question_list";

    }

    @GetMapping("/registForm")
    public String QuestionRegistForm(Model model, HttpSession session) throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        List<CoursesVO> courseList = questionService.getCourseBystudent(loginUser.getMemberId());
        model.addAttribute("courseList", courseList);

        return "question/question_regist";
    }

    @PostMapping("/regist")
    public String registQuestion(QuestionVO question, HttpSession session) throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        question.setMemberId(loginUser.getMemberId());

        questionService.registQuestion(question);

        return "redirect:/question/list";
    }

    @GetMapping("/detail")
    public String questionDetail(int questionId, Model model, HttpSession session) throws SQLException {
        QuestionVO question = questionService.getQuestionDetail(questionId);

        String viewKey = "viewed_" + questionId;
        if (session.getAttribute(viewKey)==null) {
            questionService.increaseViewCount(questionId);
            session.setAttribute(viewKey, true);

            question = questionService.getQuestionDetail(questionId);
        }

        List<CommentVO> commentList = commentService.getCommentListByQuestionId(questionId);

        model.addAttribute("question", question);
        model.addAttribute("commentList", commentList);

        return "question/question_detail";
    }

    @GetMapping("/modifyForm")
    public String modifyForm(int questionId, Model model, HttpSession session) throws SQLException {

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        QuestionVO question = questionService.getQuestionDetail(questionId);
        if (question == null || question.getMemberId() != loginUser.getMemberId()) {
            return "redirect:/question/detail?questionId=" + questionId;
        }

        List<CoursesVO> courseList = questionService.getCourseBystudent(loginUser.getMemberId());

        model.addAttribute("question", question);
        model.addAttribute("courseList", courseList);

        return "question/question_regist";
    }

    @PostMapping("/modify")
    public String modifyQeustion(QuestionVO question, HttpSession session, RedirectAttributes rttr)
            throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/common/loginForm";
        }

        QuestionVO origin = questionService.getQuestionDetail(question.getQuestionId());
        if (origin == null || origin.getMemberId() != loginUser.getMemberId()) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/question/detail?questionId=" + question.getQuestionId();
        }

        question.setMemberId(loginUser.getMemberId());
        questionService.modifyQuestion(question);

        return "redirect:/question/detail?questionId=" + question.getQuestionId();
    }

    @PostMapping("/delete/{questionId}")
    public String deleteQuestion(@PathVariable int questionId, HttpSession session, RedirectAttributes rttr)
            throws SQLException {
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null)
            return "redirect:/common/loginForm";

        QuestionVO origin = questionService.getQuestionDetail(questionId);
        if (origin == null || origin.getMemberId() != loginUser.getMemberId()) {
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
            return "redirect:/question/detail?questionId=" + questionId;
        }

        questionService.deleteQuestion(questionId);
        rttr.addFlashAttribute("msg", "삭제되었습니다.");
        return "redirect:/question/list";
    }
}
