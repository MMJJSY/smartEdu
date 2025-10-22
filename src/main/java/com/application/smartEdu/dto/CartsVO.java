package com.application.smartEdu.dto;

import java.sql.Timestamp;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CartsVO {
    private int cartId;
    private int studentId;
    private int courseId;
    private String courseAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
