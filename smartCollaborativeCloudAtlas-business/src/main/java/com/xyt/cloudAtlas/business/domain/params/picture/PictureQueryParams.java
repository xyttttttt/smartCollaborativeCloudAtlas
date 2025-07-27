package com.xyt.cloudAtlas.business.domain.params.picture;

import com.xyt.init.base.request.BaseRequest;
import com.xyt.init.base.request.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class PictureQueryParams  extends PageRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 空间 id（为空表示公共空间）
     */
    private Long spaceId;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（JSON 数组）
     */
    private List<String> tags;


    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 图片宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 图片主色调
     */
    private String picColor;

    /**
     * 审核状态：0-待审核; 1-通过; 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 缩略图 url
     */
    private String thumbnailUrl;

}
