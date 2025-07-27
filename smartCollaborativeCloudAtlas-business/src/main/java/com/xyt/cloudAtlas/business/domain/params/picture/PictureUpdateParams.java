package com.xyt.cloudAtlas.business.domain.params.picture;

import com.xyt.init.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PictureUpdateParams extends BaseRequest {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
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
     * 审核状态：0-待审核; 1-通过; 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 缩略图 url
     */
    private String thumbnailUrl;

}
