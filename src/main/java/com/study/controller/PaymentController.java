package com.study.controller;

import com.study.response.PaymentResponse;
import com.study.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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
     * 결제 승인 API 호출
     */
    @PostMapping("/api/payment/confirm/widget")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {
        JSONObject requestData = paymentService.parseRequestData(jsonBody);
        JSONObject response = paymentService.sendRequest(requestData);
        int statusCode = response.containsKey("error") ? 400 : 200;
        if (statusCode == 200) {
            paymentService.save(response);
        }
        return ResponseEntity.status(statusCode).body(response);
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