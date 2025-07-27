package com.xyt.cloudAtlas.business.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyt.cloudAtlas.business.domain.entity.picture.Picture;
import com.xyt.cloudAtlas.business.domain.entity.picture.convertor.PictureConvertor;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.exception.BasicException;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureEditParams;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureQueryParams;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureUpdateParams;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureUploadParams;
import com.xyt.cloudAtlas.business.domain.request.picture.UploadPictureRequest;
import com.xyt.cloudAtlas.business.domain.response.priture.vo.PictureVO;
import com.xyt.cloudAtlas.business.domain.service.PictureService;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.base.request.DeleteRequest;
import com.xyt.init.base.response.PageResponse;
import com.xyt.init.file.FileService;
import com.xyt.init.web.vo.MultiResult;
import com.xyt.init.web.vo.Result;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Date;
import java.util.List;

import static com.xyt.cloudAtlas.business.domain.exception.BasicErrorCode.PARAM_ERROR;
import static com.xyt.cloudAtlas.business.domain.exception.PictureErrorCode.PICTURE_NOT_EXIST;
import static com.xyt.cloudAtlas.business.domain.exception.PictureErrorCode.PICTURE_SAVE_FAILED;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "PictureController")
public class PictureController {


    @Autowired
    private PictureService pictureService;


    /**
     * 上传图片（可重新上传）
     */
    @PostMapping("/picture/upload")
    public Result<PictureVO> uploadPicture(
            @RequestPart("file") MultipartFile multipartFile,
            PictureUploadParams pictureUploadParams) {

        UploadPictureRequest uploadPictureParams = new UploadPictureRequest(pictureUploadParams.getId());
        Picture picture = pictureService.uploadPicture(multipartFile, uploadPictureParams);
        return Result.success(PictureConvertor.INSTANCE.mapToVo(picture));
    }


    /**
     * 删除图片
     */
    @PostMapping("/picture/delete")
    public Result<UserOperatorResponse> deletePicture(@RequestBody DeleteRequest deleteParams) {

        UserOperatorResponse operatorResponse = pictureService.deletePicture(deleteParams.getId());

        return Result.success(operatorResponse);
    }

    /**
     * 更新图片（仅管理员可用）
     */
    @PostMapping("/admin/picture/update")
    public Result<UserOperatorResponse> updatePicture(@Valid @RequestBody PictureUpdateParams pictureUpdateParams) {

        // 将实体类和 DTO 进行转换
        Picture picture = new Picture();
        picture.createByUpdateParams(pictureUpdateParams);
        UserOperatorResponse userOperatorResponse =  pictureService.updatePicture(picture);

        return Result.success(userOperatorResponse);
    }

    /**
     * 根据 id 获取图片（仅管理员可用）
     */
    @GetMapping("/picture/get")
    public Result<Picture> getPictureById(long id) {
        Assert.isTrue(id > 0, PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        // 查询数据库
        Picture picture = pictureService.getById(id);

        Assert.isTrue(picture != null, PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        // 获取封装类
        return Result.success(picture);
    }

    /**
     * 根据 id 获取图片（封装类）
     */
    @GetMapping("/picture/get/vo")
    public Result<PictureVO> getPictureVOById(@RequestParam @NotNull(message = "参数异常") Long id) {
        // 查询数据库
        Picture picture = pictureService.getById(id);
        Assert.isTrue(picture != null, PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        // 获取封装类
        return Result.success(PictureConvertor.INSTANCE.mapToVo(picture));
    }

    /**
     * 分页获取图片列表（仅管理员可用）
     */
    @PostMapping("/picture/list/page")
    public MultiResult<Picture> listPictureByPage(@RequestBody PictureQueryParams pictureQueryParams) {

        PageResponse<Picture> picturePage =   pictureService.listPictureByPage(pictureQueryParams);

        return MultiResult.successMulti(picturePage.getDatas(),picturePage.getTotal(), (int) picturePage.getCurrentPage(),(int)picturePage.getPageSize());
    }

    /**
     * 分页获取图片列表（封装类）
     */
    @PostMapping("/picture/list/page/vo")
    public MultiResult<PictureVO> listPictureVOByPage(@RequestBody PictureQueryParams pictureQueryParams) {
        long size = pictureQueryParams.getPageSize();
        // 限制爬虫
        Assert.isTrue(size <= 20,  PARAM_ERROR.getCode(), PARAM_ERROR.getMessage());
        // 查询数据库
        PageResponse<Picture> picturePage =   pictureService.listPictureByPage(pictureQueryParams);
        // 获取封装类
        return MultiResult.successMulti(PictureConvertor.INSTANCE.mapToVoList(picturePage.getDatas()),picturePage.getTotal(), picturePage.getCurrentPage(), picturePage.getPageSize());
    }

    /**
     * 编辑图片（给用户使用）
     */
    @PostMapping("/picture/edit")
    public Result<UserOperatorResponse> editPicture(@Valid @RequestBody PictureEditParams pictureEditParams) {
        // 在此处将实体类和 DTO 进行转换
        Picture picture = new Picture();
        picture.createByEditParams(pictureEditParams);
        // TODO 数据校验
//        pictureService.validPicture(picture);
        UserOperatorResponse  userOperatorResponse =  pictureService.editPicture(picture);

        return Result.success(userOperatorResponse);
    }


}
