package com.application.smartEdu.command;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CartAddCommand {
    private int courseId;
    private Long courseAmount; // 화면에서 가격을 보낼 수도 있고, 서버에서 Courses.price로 계산해도 됨
}//12341234
