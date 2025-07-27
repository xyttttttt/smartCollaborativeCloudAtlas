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
 * 空间
 * @TableName space
 */
@TableName(value ="space")
@Data
public class Space  extends BaseEntity {

    /**
     * 空间名称
     */
    @TableField(value = "space_name")
    private String spaceName;

    /**
     * 空间级别：0-普通版 1-专业版 2-旗舰版
     */
    @TableField(value = "space_level")
    private Integer spaceLevel;

    /**
     * 空间类型：0-私有 1-团队
     */
    @TableField(value = "space_type")
    private Integer spaceType;

    /**
     * 空间图片的最大总大小
     */
    @TableField(value = "max_size")
    private Long maxSize;

    /**
     * 空间图片的最大数量
     */
    @TableField(value = "max_count")
    private Long maxCount;

    /**
     * 当前空间下图片的总大小
     */
    @TableField(value = "total_size")
    private Long totalSize;

    /**
     * 当前空间下的图片数量
     */
    @TableField(value = "total_count")
    private Long totalCount;

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 编辑时间
     */
    @TableField(value = "edit_time")
    private Date editTime;

}