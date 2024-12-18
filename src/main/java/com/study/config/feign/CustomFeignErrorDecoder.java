package com.study.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(CustomFeignErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("[Feign ErrorDecoder] Error occurred - Method: {}, Status: {}, Reason: {}",
                methodKey, response.status(), response.reason());

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
