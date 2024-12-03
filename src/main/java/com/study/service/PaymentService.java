package com.study.service;

import static com.study.domain.PaymentStatus.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.OffsetDateTime.parse;

import com.study.domain.Payment;
import com.study.repository.PaymentRepository;
import com.study.response.PaymentResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String PAYMENT_CONFIRM_API = "https://api.tosspayments.com/v1/payments/confirm";

    private final PaymentRepository paymentRepository;
    @Value("${toss-payments.widget-secret}")
    private String WIDGET_SECRET_KEY;

    public void save(JSONObject response) {
        Payment payment = Payment.builder()
                .paymentKey(response.get("paymentKey").toString())
                .orderId(response.get("orderId").toString())
                .orderName(response.get("orderName").toString())
                .method(response.get("method").toString())
                .totalAmount((Long) response.get("totalAmount"))
                .status(valueOf(response.get("status").toString()))
                .requestedAt(parse(response.get("requestedAt").toString()).toLocalDateTime())
                .build();

        paymentRepository.save(payment);
    }

    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::new)
                .toList();
    }

    public JSONObject sendRequest(JSONObject requestData) throws IOException {
        HttpURLConnection connection = createConnection();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream()
                : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            log.error("Error reading response", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }
    }

    public JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            log.error("JSON Parsing Error", e);
            return new JSONObject();
        }
    }

    private HttpURLConnection createConnection() throws IOException {
        URL url = new URL(PAYMENT_CONFIRM_API);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization",
                "Basic " + Base64.getEncoder()
                        .encodeToString((WIDGET_SECRET_KEY + ":").getBytes(UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
}
