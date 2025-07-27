package com.xyt.cloudAtlas.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xyt.init.datasource.domain.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户邀请表
 * @TableName user_invite
 */
@TableName(value ="user_invite")
@Data
public class UserInvite  extends BaseEntity {


    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 受邀请用户 id
     */
    @TableField(value = "invite_user_id")
    private Long inviteUserId;

}