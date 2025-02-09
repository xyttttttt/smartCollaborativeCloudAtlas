package com.xyt.init.rpc.config;

import com.xyt.init.rpc.facade.FacadeAspect;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rpc 配置
 *
 * @author hollis
 */
@EnableDubbo
@Configuration
public class RpcConfiguration {

       public FacadeAspect facadeAspect() {
        return new FacadeAspect();
    }
}
