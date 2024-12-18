package com.study.config.feign;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TossPaymentsFeignConfig {

    @Bean
    public Encoder feignEncoder() {
        return new CustomFeignEncoder();
    }

    @Bean
    public Decoder feignDecoder() {
        return new CustomFeignDecoder();
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    @Bean
    public CustomFeignRequestInterceptor customFeignRequestInterceptor() {
        return new CustomFeignRequestInterceptor();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // 요청/응답 모든 로그 출력
    }
}

