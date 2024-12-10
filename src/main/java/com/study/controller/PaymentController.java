package com.study.controller;

import static java.lang.Integer.parseInt;

import com.study.response.PaymentResponse;
import com.study.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 승인 API 요청을 처리하여 HTTP 응답을 반환합니다.
     *
     * @param jsonBody 결제 요청 데이터가 포함된 JSON 문자열
     * @return 결제 요청의 결과를 나타내는 {@link PaymentResponse}
     */
    @PostMapping("/api/payment/confirm/widget")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody String jsonBody) {
        PaymentResponse response = paymentService.confirmPayment(jsonBody);
        if (response.getCode() == null) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/api/payment/widget")
    public String widget() {
        return "widget/checkout";
    }

    @GetMapping("/api/payment/success")
    public String success(@RequestParam String paymentKey,
                          @RequestParam String orderId,
                          @RequestParam String paymentType,
                          @RequestParam Integer amount,
                          Model model) {
        model.addAttribute("paymentKey", paymentKey);
        model.addAttribute("orderId", orderId);
        model.addAttribute("paymentType", paymentType);
        model.addAttribute("amount", amount);
        return "widget/success";
    }

    @GetMapping("/api/payment/fail")
    public String failPayment(@RequestParam String code,
                              @RequestParam String message,
                              Model model) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "fail";
    }

    @ResponseBody
    @GetMapping("/api/payments")
    public List<PaymentResponse> findAllPayments() {
        return paymentService.findAll();
    }
}