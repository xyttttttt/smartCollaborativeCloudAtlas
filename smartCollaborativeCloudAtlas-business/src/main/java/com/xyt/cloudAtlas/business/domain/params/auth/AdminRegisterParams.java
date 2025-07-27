package com.xyt.cloudAtlas.business.domain.params.auth;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hollis
 */
@Setter
@Getter
public class AdminRegisterParams {

    /**
     * 手机号
     */
    @Size(min = 6, max = 8)
    private String userAccount;

}
