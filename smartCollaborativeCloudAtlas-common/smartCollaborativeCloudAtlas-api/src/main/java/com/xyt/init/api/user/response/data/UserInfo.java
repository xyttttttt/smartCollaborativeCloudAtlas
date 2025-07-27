package com.xyt.init.api.user.response.data;

import com.xyt.init.api.user.constant.UserRole;
import com.xyt.init.api.user.constant.UserStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Hollis
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private int state;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 账号
     */
    private String userAccount;



    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;



    /**
     * 受邀请码
     */
    private String inviteFrom;

    /**
     * 用户简介
     */
    private String profile;


}
