package com.application.smartEdu.controller;


import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.smartEdu.dto.CommentVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.service.CommentService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/regist")
    public ResponseEntity<String> registComment(@RequestBody CommentVO comment, HttpSession session) {
        try {
            MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
            if (loginUser == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            comment.setMemberId(loginUser.getMemberId());
            commentService.regist(comment);

            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/modify")
    public ResponseEntity<String> modifyComment(@RequestBody CommentVO comment, HttpSession session) {
        try {
            MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
            if (loginUser == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            CommentVO origin = commentService.getCommentbyId(comment.getCommentId());

            if (origin.getMemberId() != loginUser.getMemberId()) {
                return new ResponseEntity<>("본인만 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
            }

            comment.setMemberId(loginUser.getMemberId());
            commentService.modify(comment);

            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment (@PathVariable int commentId, HttpSession session) {
        try {
            MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
            if (loginUser == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            CommentVO origin = commentService.getCommentbyId(commentId);

            if (origin.getMemberId() != loginUser.getMemberId()) {
                return new ResponseEntity<>("본인만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);
            }

            commentService.delete(commentId);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("list/{questionId}")
    public ResponseEntity<List<CommentVO>> getCommentList (@PathVariable int questionId) {
        try {
            List<CommentVO> commentList = commentService.getCommentListByQuestionId(questionId);
            return new ResponseEntity<>(commentList,HttpStatus.OK); 
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
