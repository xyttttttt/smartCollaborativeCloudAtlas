package com.xyt.cloudAtlas.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xyt.init.datasource.domain.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录记录表
 * @TableName user_login_record
 */
@TableName(value ="user_login_record")
@Data
@Builder
public class UserLoginRecord extends BaseEntity {

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户登录的 ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 用户登录的 userAgent
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    private Date loginTime;

}