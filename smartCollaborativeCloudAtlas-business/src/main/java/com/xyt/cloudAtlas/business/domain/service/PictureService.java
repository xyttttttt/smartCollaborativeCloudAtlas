package com.xyt.cloudAtlas.business.domain.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.cloudAtlas.business.domain.entity.picture.Picture;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureQueryParams;
import com.xyt.cloudAtlas.business.domain.request.picture.UploadPictureRequest;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.base.response.PageResponse;
import com.xyt.init.file.domain.response.UploadPictureResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author 16048
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-07-25 16:05:43
*/
public interface PictureService extends IService<Picture> {

    public Picture uploadPicture(MultipartFile multipartFile, UploadPictureRequest pictureUploadRequest);



    public void validPicture(MultipartFile multipartFile);

    UserOperatorResponse deletePicture(Long id);

    PageResponse<Picture> listPictureByPage(PictureQueryParams pictureQueryParams);

    UserOperatorResponse editPicture(Picture picture);

    UserOperatorResponse updatePicture(Picture picture);
}
