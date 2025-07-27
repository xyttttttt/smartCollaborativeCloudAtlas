package com.xyt.cloudAtlas.business.domain.entity.user;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xyt.init.api.user.constant.UserRole;
import com.xyt.init.api.user.constant.UserStateEnum;
import com.xyt.init.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户
 *
 * @author hollis
 */
@Setter
@Getter
@TableName("user")
public class User extends BaseEntity {

    /**
     * 账号
     */
    @TableField(value = "user_account")
    private String userAccount;


    /**
     * 密码
     */
    @TableField(value = "password_hash")
    private String passwordHash;

    /**
     * 用户昵称
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 邀请码
     */
    @TableField(value = "invite_code")
    private String inviteCode;

    /**
     * 受邀请码
     */
    @TableField(value = "invite_from")
    private String inviteFrom;

    /**
     * 用户简介
     */
    @TableField(value = "profile")
    private String profile;

    /**
     * 状态
     */
    @TableField(value = "status")
    private int status;

    /**
     * 用户角色：user/admin
     */
    @TableField(value = "user_role")
    private UserRole userRole;

    /**
     * 编辑时间
     */
    @TableField(value = "edit_time")
    private Date editTime;


    /**
     * 上次登录时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    public User register(String account, String userName, String password,String inviteCode,String inviterFrom) {
        this.setUserAccount(account);
        this.setUserName(userName);
        this.setPasswordHash(DigestUtil.md5Hex(password));
        this.setStatus(UserStateEnum.INIT.getValue());
        this.setUserRole(UserRole.CUSTOMER);
        this.setInviteCode(inviteCode);
        this.setInviteFrom(inviterFrom);
        return this;
    }

    public User registerAdmin(String account, String nickName, String password) {
        this.setUserAccount(account);
        this.setUserName(nickName);
        this.setPasswordHash(DigestUtil.md5Hex(password));
        this.setStatus(UserStateEnum.INIT.getValue());
        this.setUserRole(UserRole.ADMIN);
        return this;
    }


    public boolean canModifyInfo() {
        return status == UserStateEnum.INIT.getValue();
    }
}
