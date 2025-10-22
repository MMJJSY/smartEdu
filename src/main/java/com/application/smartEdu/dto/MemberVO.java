package com.application.smartEdu.dto;

import java.time.LocalDateTime;

import com.application.smartEdu.enums.Role;
import com.application.smartEdu.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MemberVO {
    private Integer memberId; // 회원번호
    private String name; // 이름
    private String email; // 이메일
    private String pwd; // 패스워드
    private String phone; // 전화번호
    private Role role; // 권한
    private String memberCode; // 회원코드
    private String image; // 프로필 이미지
    private Status status; // 회원 상태
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
    private LocalDateTime deletedAt; // 탈퇴일



    

}
