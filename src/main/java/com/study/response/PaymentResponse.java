package com.study.response;

import com.study.domain.Payment;
import lombok.Getter;

@Getter
public class PaymentResponse {

    private final String paymentKey;
    private final String orderId;
    private final String orderName;
    private final String method;
    private final Long totalAmount;
    private final String status;
    private final String requestedAt;

    public PaymentResponse(Payment payment) {
        this.paymentKey = payment.getPaymentKey();
        this.orderId = payment.getOrderId();
        this.orderName = payment.getOrderName();
        this.method = payment.getMethod();
        this.totalAmount = payment.getTotalAmount();
        this.status = payment.getStatus().name();
        this.requestedAt = String.valueOf(payment.getRequestedAt());
    }
}
