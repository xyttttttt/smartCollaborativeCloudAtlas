package com.xyt.init.api.user.request.condition;

import lombok.*;

/**
 * @author Hollis
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneAndPasswordQueryCondition implements UserQueryCondition {

    private static final long serialVersionUID = 1L;

    /**
     * 用户手机号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;
}
