package com.application.smartEdu.dto;

import java.sql.Timestamp;

import com.application.smartEdu.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVO {

    private int paymentId;
    private int studentId;
    private int courseId;
    private Long courseAmount;   // VARCHAR(512)이지만 숫자 문자열 가정 → Long으로 사용
    private PaymentStatus status; // ✅ ENUM으로 변경
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
