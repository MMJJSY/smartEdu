package com.application.smartEdu.command;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CartAddCommand {
    private int courseId;
    private Long courseAmount;
}
