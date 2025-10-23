package com.application.smartEdu.command;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRefundCommand {
    private int paymentId;
}
