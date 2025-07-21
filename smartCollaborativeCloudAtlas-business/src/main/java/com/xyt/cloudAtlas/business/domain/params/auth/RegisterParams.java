package com.xyt.cloudAtlas.business.domain.params.auth;

import com.xyt.init.base.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hollis
 */
@Setter
@Getter
public class RegisterParams {

    /**
     * 手机号
     */
    @IsMobile
    private String telephone;

    /**
     * 验证码
     */
//    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 密码
     */
    private String password;
}
