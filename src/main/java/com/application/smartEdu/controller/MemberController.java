package com.application.smartEdu.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.smartEdu.command.InstructorRegistCommand;
import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.command.StudentRegistCommand;
import com.application.smartEdu.dto.InstructorVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.enums.PendingStatus;
import com.application.smartEdu.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Value("${member.resume}")
    private String resumePath;

    @GetMapping("/registForm")
    public String registFormSelect() {
        String url = "/member/registSelect";
        return url;
    }

    @GetMapping("/registForm/student")
    public String registFormStudent() {
        String url = "/member/registstudent";
        return url;
    }

    @GetMapping(value = "/emailCheck", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String emailCheck(String email) throws Exception {
        String message = "duplicated";

        MemberVO member = memberService.getMember(email);

        if (member == null) {
            message = "";
        }

        return message;
    }

    @PostMapping("/regist/student")
    public String registStudent(StudentRegistCommand regcommand) throws Exception {
        String url = "/member/regist_success";

        MemberVO member = regcommand.toMemberVO();

        memberService.regist(member);

        return url;
    }

    @GetMapping("/registForm/instructor")
    public String registFormInstructor() {
        String url = "/member/registInstructor";

        return url;
    }

    @PostMapping("/regist/instructor")
    public String registInstructor(InstructorRegistCommand registCommand) throws Exception {
        String url = "/member/regist_success";

        MemberVO member = registCommand.toMemberVO();
        memberService.regist(member);

        MultipartFile resumeFile = registCommand.getResume();
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new Exception("이력서를 반드시 첨부해야 합니다.");
        }
        String resumeFileName = null;
        if (resumeFile != null && !resumeFile.isEmpty()) {
            resumeFileName = saveResume("", resumeFile);

            InstructorVO instructor = registCommand.toInstructorVO(member.getMemberId());
            instructor.setResume(resumeFileName);
            memberService.registInstructor(instructor);

        }
        return url;
    }

    public String saveResume(String oldResume, MultipartFile multi) throws Exception {
        // 업로드 폴더
        String uploadPath = this.resumePath;

        // 기존 파일 삭제
        if (oldResume != null && !oldResume.isEmpty()) {
            File oldFile = new File(uploadPath, oldResume);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        if (multi == null || multi.getSize() == 0) {
            return null;
        }

        // 고유 파일명 생성
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String originalName = multi.getOriginalFilename();
        String fileName = uuid + "_" + originalName;

        // 파일 저장
        File storeFile = new File(uploadPath, fileName);
        if (!storeFile.getParentFile().exists()) {
            storeFile.getParentFile().mkdirs();
        }
        multi.transferTo(storeFile);

        return fileName;

    }

    // ==========================
    // 관리자 - 강사 승인 상태 관리
    // ==========================
    @GetMapping("/dashboard/instructor_status")
    public String instructorStatusList(
            @RequestParam(value = "filter", required = false) String filter,
            @ModelAttribute("pageMaker") PageMaker pageMaker,
            Model model) throws Exception {

        // ✅ 상태 필터: searchType='s', keyword=filter(비어있으면 전체)
        pageMaker.setSearchType("s");
        pageMaker.setKeyword(filter == null ? "" : filter.trim());

        List<InstructorVO> instructorList = memberService.findPendingInstructors(pageMaker);
        int totalCount = memberService.countPendingInstructors(pageMaker);
        pageMaker.setTotalCount(totalCount);

        model.addAttribute("instructorList", instructorList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("selectedFilter", filter == null ? "" : filter.trim());

        return "dashboard/instructor_status";
    }

    @GetMapping("/detail")
    public String instructorDetail(@RequestParam("instructorId") int instructorId, Model model) throws SQLException {
        InstructorVO instructor = memberService.getInstructor(instructorId);
        model.addAttribute("instructor", instructor);
        model.addAttribute("pageTitle", "강사 상세보기");
        return "dashboard/instructor_detail";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("instructorId") int instructorId,
            @RequestParam("status") String status,
            RedirectAttributes rttr) throws SQLException {

        InstructorVO vo = new InstructorVO();
        vo.setInstructorId(instructorId);
        vo.setPendingStatus(PendingStatus.valueOf(status));
        memberService.updatePendingStatus(vo);

        rttr.addFlashAttribute("message", "강사 상태가 변경되었습니다.");
        return "redirect:/member/dashboard/instructor_status";
    }

}
