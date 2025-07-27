package com.xyt.cloudAtlas.business.domain.params.picture;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureUploadParams implements Serializable {

    /**
     * 图片 id（用于修改）
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
