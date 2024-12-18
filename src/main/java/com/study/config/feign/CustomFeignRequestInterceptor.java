package com.study.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomFeignRequestInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CustomFeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-Type", "application/json");
        log.info("[Feign RequestInterceptor] Request Method: {}, URL: {}", template.method(), template.url());
    }
}

