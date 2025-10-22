package com.application.smartEdu.command;

import org.springframework.web.multipart.MultipartFile;

import com.application.smartEdu.dto.InstructorVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.enums.PendingStatus;
import com.application.smartEdu.enums.Role;
import com.application.smartEdu.enums.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class InstructorRegistCommand {
    private String email;
    private String pwd;
    private String name;
    private String[] phone;
    private MultipartFile resume;
   
     public MemberVO toMemberVO() {

        MemberVO member = MemberVO.builder()
                .email(email)
                .pwd(pwd)
                .name(name)
                .role(Role.INSTRUCTOR)
                .status(Status.ACTIVE)
                .build();

        String phoneTemp = "";
        for(String p : phone) {
            phoneTemp += p;
        }        
        member.setPhone(phoneTemp);

        return member;
    }

    public InstructorVO toInstructorVO(Integer member_id) {
        return InstructorVO.builder()
                .instructorId(member_id)
                .pendingStatus(PendingStatus.PENDING)
                .build();
    }
}

