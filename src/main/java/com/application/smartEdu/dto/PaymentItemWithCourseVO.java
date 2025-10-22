package com.application.smartEdu.dto;

import java.sql.Timestamp;

import com.application.smartEdu.enums.CourseCategory;
import com.application.smartEdu.enums.PaymentStatus;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentItemWithCourseVO {

    // Payments
    private int paymentId;
    private int studentId;
    private int courseId;
    private Long courseAmount;         // CAST로 숫자 매핑
    private PaymentStatus status;      // NORMAL / CANCELLED / REFUNDED
    private Timestamp createdAt;

    // Courses
    private String courseTitle;
    private CourseCategory courseCategory;
    private String instructorName;

    // 화면 편의용(서비스에서 세팅 권장)
    private Boolean refundable;        // 14일 이내 & 시청 이전 등 정책 반영
}
