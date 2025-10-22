package com.application.smartEdu.command;

import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.enums.Role;
import com.application.smartEdu.enums.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StudentRegistCommand {
    private String email;
    private String pwd;
    private String name;
    private String[] phone;


    public MemberVO toMemberVO() {

        MemberVO member = MemberVO.builder()
                .email(email)
                .pwd(pwd)
                .name(name)
                .role(Role.STUDENT)
                .status(Status.ACTIVE)
                .build();

        String phoneTemp = "";
        for(String p : phone) {
            phoneTemp += p;
        }        
        member.setPhone(phoneTemp);

        return member;

    }
}
