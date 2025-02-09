package com.xyt.init.sms.config;

import com.xyt.init.sms.MockSmsServiceImpl;
import com.xyt.init.sms.SmsService;
import com.xyt.init.sms.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Hollis
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfiguration {

    @Autowired
    private SmsProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = SmsProperties.PREFIX, value = "enabled", havingValue = "true")
    @Profile({"default","prod"})
    public SmsService smsService() {
        SmsServiceImpl smsService = new SmsServiceImpl();
        smsService.setHost(properties.getHost());
        smsService.setPath(properties.getPath());
        smsService.setAppcode(properties.getAppcode());
        smsService.setSmsSignId(properties.getSmsSignId());
        smsService.setTemplateId(properties.getTemplateId());
        return smsService;
    }

    @Bean
    @ConditionalOnMissingBean
    @Profile({"dev","test"})
    public SmsService mockSmsService() {
        MockSmsServiceImpl smsService = new MockSmsServiceImpl();
        return smsService;
    }

}
