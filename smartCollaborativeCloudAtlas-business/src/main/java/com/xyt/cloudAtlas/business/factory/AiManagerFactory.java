package com.xyt.cloudAtlas.business.factory;


import com.xyt.cloudAtlas.business.domain.constant.AiChannelConstant;
import com.xyt.init.base.utils.BeanNameUtils;
import com.xyt.cloudAtlas.business.domain.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AiManagerFactory {


    @Autowired
    private final Map<String, AiService> serviceMap = new ConcurrentHashMap<String, AiService>();

    @Value("${spring.profiles.active}")
    private String profile;

    public AiService get(AiChannelConstant aiChannelConstant) {


        String beanName = BeanNameUtils.getBeanName(aiChannelConstant.name(), "AiServiceImpl");

        //组装出beanName，并从map中获取对应的bean
        AiService aiService = serviceMap.get(beanName);

        if (aiService != null) {
            return aiService;
        } else {
            throw new UnsupportedOperationException(
                    "No PayChannelService Found With payChannel : " + aiChannelConstant + " , beanName : " + beanName);
        }
    }
}
