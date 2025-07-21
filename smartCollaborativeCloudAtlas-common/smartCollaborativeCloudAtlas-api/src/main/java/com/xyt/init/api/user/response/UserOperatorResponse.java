package com.xyt.init.api.user.response;

import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @author Hollis
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
