package com.xyt.cloudAtlas.business.domain.request.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户认证参数
 *
 * @author hollis
 */
@Setter
@Getter
public class UserAuthRequest {

    private Long userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;

}
