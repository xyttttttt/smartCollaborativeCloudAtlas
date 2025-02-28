package com.xyt.init.business.domain.params.auth;

import com.xyt.init.business.domain.constant.LoginType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hollis
 */
@Setter
@Getter
public class LoginParams extends RegisterParams {

    /**
     * 记住我
     */
    private Boolean rememberMe;


    /**
     * 登录类型
     */
    @NotBlank(message = "登录类型不能为空")
    private LoginType loginType;
}
