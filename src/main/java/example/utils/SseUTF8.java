package com.utils;//package com.utils;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import jakarta.validation.constraints.NotNull;
//import java.nio.charset.StandardCharsets;
//
///**
// * @Description 防止中文乱码
// */
//public class SseUTF8 extends SseEmitter {
//
//    public SseUTF8(Long timeout) {
//        super(timeout);
//    }
//
//    @Override
//    protected void extendResponse(@NotNull ServerHttpResponse outputMessage) {
//        super.extendResponse(outputMessage);
//        HttpHeaders headers = outputMessage.getHeaders();
//        headers.setContentType(new MediaType(MediaType.TEXT_EVENT_STREAM, StandardCharsets.UTF_8));
//    }
//
//}