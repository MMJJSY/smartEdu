package com.application.smartEdu.dto;

import com.application.smartEdu.enums.PendingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorVO {
    private Integer instructorId; // 강사번호
    private String resume; // 이력서
    private PendingStatus pendingStatus; // 승인상태

    private String name;   // 회원 이름
    private String email;  // 회원 이메일
    private String phone;  // ✅ 전화번호 추가
    

}
