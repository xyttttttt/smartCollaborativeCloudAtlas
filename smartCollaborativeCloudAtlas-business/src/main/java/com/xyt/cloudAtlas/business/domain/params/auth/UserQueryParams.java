package com.xyt.cloudAtlas.business.domain.params.auth;

import com.xyt.init.base.request.PageRequest;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hollis
 */
@Setter
@Getter
public class UserQueryParams extends PageRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;
}
