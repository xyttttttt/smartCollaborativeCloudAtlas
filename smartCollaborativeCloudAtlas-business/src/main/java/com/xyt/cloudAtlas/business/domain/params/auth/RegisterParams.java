package com.xyt.cloudAtlas.business.domain.params.auth;

import com.xyt.init.base.validator.IsMobile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 6, max = 14)
    private String userAccount;


    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 密码
     */
    @NotBlank
    private String checkPassword;
}
