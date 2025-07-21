package com.xyt.cloudAtlas.business.domain.params.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户修改参数
 *
 * @author hollis
 */
@Setter
@Getter
public class UserModifyPasswordParams {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
