package com.xyt.cloudAtlas.business.domain.entity.space;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.xyt.init.datasource.domain.entity.BaseEntity;
import lombok.Data;

/**
 * 空间用户关联
 * @TableName space_user
 */
@TableName(value ="space_user")
@Data
public class SpaceUser  extends BaseEntity {

    /**
     * 空间 id
     */
    @TableField(value = "space_id")
    private Long spaceId;

    /**
     * 用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 空间角色：viewer/editor/admin
     */
    @TableField(value = "space_role")
    private String spaceRole;
}