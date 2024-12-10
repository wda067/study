package com.study.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PaymentRequest {

    @NotBlank
    private final String paymentKey;

    @NotBlank
    private final String orderId;

    @NotBlank
    private final String amount;

    public PaymentRequest(String paymentKey, String orderId, String amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
