package com.xyt.init.ai.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai.zhipu")
@Data
public class ZhiPuAiConfig {

    /**
     * apiKey,从平台获取
     */
    private String apiKey;

    //初始化客户端bean
    @Bean
    public ClientV4 clientV4() {
       return new ClientV4.Builder(apiKey).build();
    }
}
