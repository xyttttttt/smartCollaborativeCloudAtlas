package com.xyt.cloudAtlas.business.domain.request.picture;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadPictureRequest {

    /**
     * 图片 id（用于修改）
     */
    private Long id;

}
