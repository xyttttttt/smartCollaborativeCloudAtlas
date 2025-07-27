package com.xyt.init.web.configuration;

import com.xyt.init.web.handler.GlobalWebExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Hollis
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    GlobalWebExceptionHandler globalWebExceptionHandler() {
        return new GlobalWebExceptionHandler();
    }


    /**
     * 注册token过滤器
     *
     * @param redissonClient
     * @return
     */
//    @Bean
//    public FilterRegistrationBean<TokenFilter> tokenFilter(RedissonClient redissonClient) {
//        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(new TokenFilter(redissonClient));
//        registrationBean.addUrlPatterns("/trade/buy");
//        registrationBean.setOrder(10);
//
//        return registrationBean;
//    }

}
