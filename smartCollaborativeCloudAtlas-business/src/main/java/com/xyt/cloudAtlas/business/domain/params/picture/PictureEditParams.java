package com.xyt.cloudAtlas.business.domain.params.picture;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xyt.init.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class PictureEditParams extends BaseRequest {

    /**
     * id
     */
    @NotNull(message = "参数异常")
    private Long id;



    /**
     * 图片名称
     */
    private String name;


    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（JSON 数组）
     */
    private List<String> tags;


    /**
     * 图片主色调
     */
    private String picColor;


    /**
     * 缩略图 url
     */
    private String thumbnailUrl;


}
