package com.study.service;

import static com.study.domain.PaymentStatus.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.OffsetDateTime.parse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.client.TossPaymentsClient;
import com.study.domain.Payment;
import com.study.repository.PaymentRepository;
import com.study.request.PaymentRequest;
import com.study.response.PaymentResponse;
import feign.FeignException;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentsClient tossPaymentsClient;
    private final ObjectMapper objectMapper;

    @Value("${toss-payments.widget-secret}")
    private String widgetSecretKey;

    public PaymentResponse confirmPayment(String jsonBody) {
        try {
            PaymentResponse response = sendRequest(jsonBody);
            paymentRepository.save(mapToPayment(response));
            return response;
        } catch (FeignException e) {
            try {
                return objectMapper.readValue(e.contentUTF8(), PaymentResponse.class);
            } catch (JsonProcessingException jsonProcessingException) {
                return new PaymentResponse("UNKNOWN_ERROR", "알 수 없는 오류가 발생했습니다.");
            }
        }
    }

    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::new)
                .toList();
    }

    /**
     * JSON 문자열을 PaymentRequest 객체로 변환하고, 토스 결제 승인 API를 호출합니다.
     *
     * @param jsonBody
     * @return
     */
    private PaymentResponse sendRequest(String jsonBody) {
        String authorizationHeader =
                "Basic " + Base64.getEncoder().encodeToString((widgetSecretKey + ":").getBytes(UTF_8));
        try {
            PaymentRequest request = objectMapper.readValue(jsonBody, PaymentRequest.class);
            return tossPaymentsClient.confirmPayment(authorizationHeader, request);
        } catch (JsonProcessingException e) {
            return new PaymentResponse("UNKNOWN_ERROR", "알 수 없는 오류가 발생했습니다.");
        }
    }

    private Payment mapToPayment(PaymentResponse response) {
        return Payment.builder()
                .paymentKey(response.getPaymentKey())
                .orderId(response.getOrderId())
                .orderName(response.getOrderName())
                .method(response.getMethod())
                .totalAmount(response.getTotalAmount())
                .status(valueOf(response.getStatus()))
                .requestedAt(parse(response.getRequestedAt()).toLocalDateTime())
                .build();
    }
}
