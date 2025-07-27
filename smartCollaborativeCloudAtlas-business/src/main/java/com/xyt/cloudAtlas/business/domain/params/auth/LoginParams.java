package com.xyt.cloudAtlas.business.domain.params.auth;

import com.xyt.cloudAtlas.business.domain.constant.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hollis
 */
@Setter
@Getter
public class LoginParams{

    /**
     * 记住我
     */
    private Boolean rememberMe;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 手机号
     */
    @Size(min = 6, max = 14)
    private String userAccount;

}
