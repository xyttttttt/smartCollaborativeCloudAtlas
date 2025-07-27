package com.xyt.cloudAtlas.business.domain.entity.picture;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.xyt.cloudAtlas.business.domain.entity.picture.convertor.PictureConvertor;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureEditParams;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureUpdateParams;
import com.xyt.init.file.domain.response.UploadPictureResponse;
import lombok.Data;

/**
 * 图片
 * @TableName picture
 */
@TableName(value ="picture")
@Data
public class Picture implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片 url
     */
    @TableField(value = "url")
    private String url;

    /**
     * 图片名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 空间 id（为空表示公共空间）
     */
    @TableField(value = "space_id")
    private Long spaceId;

    /**
     * 简介
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 分类
     */
    @TableField(value = "category")
    private String category;

    /**
     * 标签（JSON 数组）
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 图片体积
     */
    @TableField(value = "pic_size")
    private Long picSize;

    /**
     * 图片宽度
     */
    @TableField(value = "pic_width")
    private Integer picWidth;

    /**
     * 图片高度
     */
    @TableField(value = "pic_height")
    private Integer picHeight;

    /**
     * 图片宽高比例
     */
    @TableField(value = "pic_scale")
    private Double picScale;

    /**
     * 图片格式
     */
    @TableField(value = "pic_format")
    private String picFormat;

    /**
     * 图片主色调
     */
    @TableField(value = "pic_color")
    private String picColor;

    /**
     * 审核状态：0-待审核; 1-通过; 2-拒绝
     */
    @TableField(value = "review_status")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @TableField(value = "review_message")
    private String reviewMessage;

    /**
     * 审核人 ID
     */
    @TableField(value = "reviewer_id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @TableField(value = "review_time")
    private Date reviewTime;

    /**
     * 缩略图 url
     */
    @TableField(value = "thumbnail_url")
    private String thumbnailUrl;

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 编辑时间
     */
    @TableField(value = "edit_time")
    private Date editTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public Picture buildUploadPicture(UploadPictureResponse uploadPictureResponse,Long userId) {

        this.setUrl(uploadPictureResponse.getUrl());
        this.setName(uploadPictureResponse.getPicName());
        this.setPicSize(uploadPictureResponse.getPicSize());
        this.setPicWidth(uploadPictureResponse.getPicWidth());
        this.setPicHeight(uploadPictureResponse.getPicHeight());
        this.setPicScale(uploadPictureResponse.getPicScale());
        this.setPicFormat(uploadPictureResponse.getPicFormat());
        this.setUserId(userId);
        return this;
    }

    public Picture createByEditParams(PictureEditParams pictureEditParams) {
        PictureConvertor.INSTANCE.editMapToEntity(pictureEditParams);
        this.setEditTime(new Date());
        return  this;
    }

    public Picture createByUpdateParams(PictureUpdateParams pictureUpdateParams) {
        PictureConvertor.INSTANCE.updateMapToEntity(pictureUpdateParams)
        return this;
    }
}