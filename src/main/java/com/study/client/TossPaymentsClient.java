package com.study.client;

import com.study.request.PaymentRequest;
import com.study.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossPaymentsClient", url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentsClient {

    @PostMapping(value = "/confirm")
    PaymentResponse confirmPayment(@RequestHeader("Authorization") String authorization,
                                   @RequestBody PaymentRequest requestData);
}
