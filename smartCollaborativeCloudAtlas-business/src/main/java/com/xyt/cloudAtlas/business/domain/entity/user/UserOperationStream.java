package com.xyt.cloudAtlas.business.domain.entity.user;


import com.baomidou.mybatisplus.annotation.TableField;
import com.xyt.init.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 用户操作流水
 * </p>
 *
 * @author wswyb001
 * @since 2024-01-13
 */
@Getter
@Setter
public class UserOperationStream extends BaseEntity {


    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户操作类型：user/admin
     */
    @TableField(value = "type")
    private String type;

    /**
     * 参数
     */
    @TableField(value = "param")
    private String param;

    /**
     * 扩展字段
     */
    @TableField(value = "extend_info")
    private String extendInfo;

    /**
     * 操作时间时间
     */
    @TableField(value = "operate_time")
    private Date operateTime;

}
