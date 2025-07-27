package com.xyt.init.api.user.request;

import com.xyt.init.api.user.request.condition.UserIdQueryCondition;
import com.xyt.init.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import com.xyt.init.api.user.request.condition.UserPhoneQueryCondition;
import com.xyt.init.api.user.request.condition.UserQueryCondition;
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
public class UserQueryRequest extends BaseRequest {

    private UserQueryCondition userQueryCondition;

    public UserQueryRequest(Long userId) {
        UserIdQueryCondition userIdQueryCondition = new UserIdQueryCondition();
        userIdQueryCondition.setUserId(userId);
        this.userQueryCondition = userIdQueryCondition;
    }

    public UserQueryRequest(String telephone) {
        UserPhoneQueryCondition userPhoneQueryCondition = new UserPhoneQueryCondition();
        userPhoneQueryCondition.setTelephone(telephone);
        this.userQueryCondition = userPhoneQueryCondition;
    }

    public UserQueryRequest(String userAccount, String password) {
        UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition = new UserPhoneAndPasswordQueryCondition();
        userPhoneAndPasswordQueryCondition.setUserAccount(userAccount.trim());
        userPhoneAndPasswordQueryCondition.setPassword(password.trim());
        this.userQueryCondition = userPhoneAndPasswordQueryCondition;
    }

}
