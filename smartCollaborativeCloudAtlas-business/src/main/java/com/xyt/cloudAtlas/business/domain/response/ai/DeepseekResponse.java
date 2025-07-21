package com.xyt.cloudAtlas.business.domain.response.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeepseekResponse {
    private String id; // 唯一标识
    private String object; // 对象类型
    private long created; // 创建时间戳
    private String model; // 模型名称
    private List<Choice> choices; // 返回结果选项
    private Usage usage; // token 使用情况

    @Data
    public static class Choice {
        private int index; // 选项索引
        private Delta delta; // 增量内容
        @JsonProperty("finish_reason")
        private String finishReason; // 结束原因
    }

    @Data
    public static class Delta {
        private String role; // 角色
        private String content; // 内容
        @JsonProperty("reasoning_content") // 显式映射 JSON 字段
        private String reasoningContent; // 新增字段
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens; // 输入 token 数量
        @JsonProperty("completion_tokens")
        private int completionTokens; // 输出 token 数量
        @JsonProperty("total_tokens")
        private int totalTokens; // 总 token 数量
    }

    @Override
    public String toString() {
        return "DeepseekResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", choices=" + choices +
                ", usage=" + usage +
                '}';
    }
}
