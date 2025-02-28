package com.xyt.init.business.domain.request.ai;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TenDeepSeekRequest {


    private String model = "deepseek-r1";


    private List<Message> messages;


    private Boolean stream;


    public static TenDeepSeekRequest buildTenDeepSeekRequest(String question) {
        return TenDeepSeekRequest.builder()
                .model("deepseek-r1")
                .messages(List.of(Message.builder().role("user").content(question).build()))
                .stream(true)
                .build();
    }


    @Data
    @Builder
    public static class Message {
        private String role;

        private String content;
    }
}
