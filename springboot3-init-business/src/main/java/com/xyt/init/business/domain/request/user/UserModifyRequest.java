package com.xyt.init.business.domain.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户修改参数
 *
 * @author hollis
 */
@Setter
@Getter
public class UserModifyRequest {


    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 头像
     */
    private String profilePhotoUrl;

}
