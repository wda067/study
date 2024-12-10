package com.study.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.domain.Payment;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  //null인 필드는 JSON에서 제외
public class PaymentResponse {

    private final String paymentKey;
    private final String orderId;
    private final String orderName;
    private final String method;
    private final Long totalAmount;
    private final String status;
    private final String requestedAt;

    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;

    public PaymentResponse(Payment payment) {
        this.paymentKey = payment.getPaymentKey();
        this.orderId = payment.getOrderId();
        this.orderName = payment.getOrderName();
        this.method = payment.getMethod();
        this.totalAmount = payment.getTotalAmount();
        this.status = payment.getStatus().name();
        this.requestedAt = String.valueOf(payment.getRequestedAt());
    }

    @JsonCreator
    public PaymentResponse(String paymentKey, String orderId, String orderName, String method, Long totalAmount,
                           String status, String requestedAt) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.method = method;
        this.totalAmount = totalAmount;
        this.status = status;
        this.requestedAt = requestedAt;
    }

    public PaymentResponse(String code, String message) {
        this.paymentKey = null;
        this.orderId = null;
        this.orderName = null;
        this.method = null;
        this.totalAmount = null;
        this.status = null;
        this.requestedAt = null;
        this.code = code;
        this.message = message;
    }
}
