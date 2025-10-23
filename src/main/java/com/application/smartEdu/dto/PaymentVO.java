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
    private Long courseAmount;   
    private PaymentStatus status; 
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
