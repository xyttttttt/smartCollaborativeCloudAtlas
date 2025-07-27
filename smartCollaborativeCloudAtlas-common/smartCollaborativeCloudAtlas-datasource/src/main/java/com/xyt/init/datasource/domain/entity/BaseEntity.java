package com.xyt.init.datasource.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

/**
 * 通用实体类
 *
 * @author Hollis
 */
@Setter
@Getter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_delete",fill = FieldFill.INSERT)
    private Integer isDelete;

    /**
     * 版本号
     */
    @Version
    @TableField(value = "version",fill = FieldFill.INSERT)
    private Integer version;


}
