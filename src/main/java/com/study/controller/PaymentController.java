package com.study.controller;

import com.study.response.PaymentResponse;
import com.study.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
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
    public String index() {
        return "widget/checkout";
    }

    @GetMapping("/api/payment/fail")
    public String failPayment(HttpServletRequest request, Model model) {
        model.addAttribute("code", request.getParameter("code"));
        model.addAttribute("message", request.getParameter("message"));
        return "fail";
    }

    @ResponseBody
    @GetMapping("/api/payments")
    public List<PaymentResponse> findAllPayments() {
        return paymentService.findAll();
    }
}