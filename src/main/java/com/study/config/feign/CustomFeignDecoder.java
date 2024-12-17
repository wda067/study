package com.study.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.Decoder;
import java.io.InputStream;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomFeignDecoder implements Decoder {

    private static final Logger log = LoggerFactory.getLogger(CustomFeignDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) {
        try (InputStream inputStream = response.body().asInputStream()) {
            if (inputStream == null) {
                log.warn("[Feign Decoder] Response body is null");
                return null;
            }

            Object result = objectMapper.readValue(inputStream, objectMapper.constructType(type));
            log.info("[Feign Decoder] Decoded Response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Feign Decoder] Decoding error: ", e);
            throw new RuntimeException(e);
        }
    }
}


