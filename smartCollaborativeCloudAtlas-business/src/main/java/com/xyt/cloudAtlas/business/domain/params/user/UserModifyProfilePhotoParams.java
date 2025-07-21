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
public class UserModifyProfilePhotoParams {

    /**
     * 头像
     */
    private String profilePhotoUrl;

}
