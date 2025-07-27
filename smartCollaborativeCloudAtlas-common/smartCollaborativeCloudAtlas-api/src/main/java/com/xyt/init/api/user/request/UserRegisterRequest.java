package com.xyt.init.api.user.request;

import com.xyt.init.base.request.BaseRequest;
import lombok.*;

/**
 * @author Hollis
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest extends BaseRequest {

    private String userAccount;

    private String inviteCode;

    private String password;

    /**
     * 密码
     */
    private String checkPassword;

}
