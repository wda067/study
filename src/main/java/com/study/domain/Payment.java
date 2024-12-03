package com.study.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentKey;

    private String orderId;

    private String orderName;

    private String method;

    private Long totalAmount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    private LocalDateTime requestedAt;

    @Builder
    public Payment(String paymentKey, String orderId, String orderName, String method, Long totalAmount, Member member,
                   PaymentStatus status, LocalDateTime requestedAt) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.method = method;
        this.totalAmount = totalAmount;
        this.member = member;
        this.status = status;
        this.requestedAt = requestedAt;
    }
}
