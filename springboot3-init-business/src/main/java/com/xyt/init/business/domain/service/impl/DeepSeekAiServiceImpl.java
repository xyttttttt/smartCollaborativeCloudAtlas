package com.xyt.init.business.domain.service.impl;





import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyt.init.business.domain.request.ai.TenDeepSeekRequest;
import com.xyt.init.business.domain.response.ai.DeepseekResponse;
import com.xyt.init.business.domain.service.AiService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service("deepseekAiServiceImpl")
public class DeepSeekAiServiceImpl implements AiService {


    @Value("${ai.ten.apiKey}")
    private String apiKey;

    @Override
    public String chat(String question, Consumer<String> onMessage) {

        TenDeepSeekRequest tenDeepSeekRequest = TenDeepSeekRequest.buildTenDeepSeekRequest(question);

        String requestBody = JSONUtil.toJsonStr(tenDeepSeekRequest);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody);
            Request request = new Request.Builder()
                    .url("https://api.lkeap.cloud.tencent.com/v1/chat/completions")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("X-TC-Action", "QueryRewrite")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 用 try-with-resources 自动关闭流
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try (ResponseBody responseBody = response.body();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 判断每行是否为有效数据
                    if (line.startsWith("data:") && !line.equals("data: [DONE]")) {
                        String json = line.substring(6).trim();
                        try {
                            DeepseekResponse deepseekResponse = mapper.readValue(json, DeepseekResponse.class);
                            if (deepseekResponse.getChoices().get(0).getFinishReason() != null) {
                                onMessage.accept("the session end");
                            }
                            // 处理 ReasoningContent
                            if (deepseekResponse.getChoices().get(0).getDelta().getReasoningContent() != null) {
                                onMessage.accept(deepseekResponse.getChoices().get(0).getDelta().getReasoningContent());
                            }
                            // 处理 Content
                            if (deepseekResponse.getChoices().get(0).getDelta().getContent() != null) {
                                onMessage.accept(deepseekResponse.getChoices().get(0).getDelta().getContent());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析流式返回的 JSON 数据
     *
     * @param streamResponse 流式返回的 JSON 数据
     * @return 解析后的对象集合
     */
    private List<DeepseekResponse> parseStreamResponse(String streamResponse) {
        List<DeepseekResponse> deepseekResponseList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 忽略未知字段

        // 按行分割数据
        String[] lines = streamResponse.split("\n");
        for (String line : lines) {
            if (line.startsWith("data:") && !line.equals("data: [DONE]")) {
                try {
                    // 去除 "data:" 前缀并解析 JSON
                    String json = line.substring(6).trim();
                    DeepseekResponse deepseekResponse = mapper.readValue(json, DeepseekResponse.class);
                    deepseekResponseList.add(deepseekResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return deepseekResponseList;
    }
}
