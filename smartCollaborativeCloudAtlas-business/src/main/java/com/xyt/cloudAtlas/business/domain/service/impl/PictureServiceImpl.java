package com.xyt.cloudAtlas.business.domain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.cloudAtlas.business.domain.entity.picture.Picture;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureQueryParams;
import com.xyt.cloudAtlas.business.domain.request.picture.UploadPictureRequest;
import com.xyt.cloudAtlas.business.domain.service.PictureService;
import com.xyt.cloudAtlas.business.domain.service.UserService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.PictureMapper;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.base.exception.BizException;
import com.xyt.init.base.exception.SystemException;
import com.xyt.init.base.response.PageResponse;
import com.xyt.init.file.FileService;
import com.xyt.init.file.domain.response.UploadPictureResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.xyt.cloudAtlas.business.domain.exception.PictureErrorCode.*;
import static com.xyt.init.base.exception.BizErrorCode.UPLOAD_FILE_FAILED;
import static com.xyt.init.base.exception.RepoErrorCode.UPDATE_FAILED;

/**
 * @author 16048
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2025-07-25 16:05:43
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {


    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Override
    public Picture uploadPicture(MultipartFile multipartFile, UploadPictureRequest pictureUploadRequest) {

        String userId = (String) StpUtil.getLoginId();
        // 用于判断是新增还是更新图片
        Long pictureId = null;
        if (pictureUploadRequest != null) {
            pictureId = pictureUploadRequest.getId();
        }
        // 如果是更新图片，需要校验图片是否存在
        if (pictureId != null) {
            boolean exists = this.lambdaQuery()
                    .eq(Picture::getId, pictureId)
                    .exists();
            Assert.isTrue(exists, PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        }
        // 上传图片，得到信息
        // 按照用户 id 划分目录
        String uploadPathPrefix = String.format("public/%s", userId);
        UploadPictureResponse uploadPictureResponse = null;
        try {
            uploadPictureResponse = uploadPicture(multipartFile, uploadPathPrefix);
        } catch (IOException e) {
            throw new SystemException(UPLOAD_FILE_FAILED);
        }
        // 构造要入库的图片信息
        Picture picture = new Picture();
        picture.buildUploadPicture(uploadPictureResponse, Long.valueOf(userId));
        // 如果 pictureId 不为空，表示更新，否则是新增
        if (pictureId != null) {
            // 如果是更新，需要补充 id 和编辑时间
            picture.setId(pictureId);
            picture.setEditTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);

        Assert.isTrue(result, PICTURE_SAVE_FAILED.getCode(), PICTURE_SAVE_FAILED.getMessage());
        return picture;
    }


    /**
     * 上传图片
     *
     * @param file             文件
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    private UploadPictureResponse uploadPicture(MultipartFile file, String uploadPathPrefix) throws IOException {
        // 校验图片
        validPicture(file);
        // 图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originFilename = file.getOriginalFilename();
        InputStream fileStream = file.getInputStream();
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);

        return fileService.upload(uploadPath, fileStream);
    }

    /**
     * 校验文件
     *
     * @param multipartFile multipart 文件
     */
    @Override
    public void validPicture(MultipartFile multipartFile) {
        Assert.isTrue(multipartFile != null, EMPTY_FILE_EXIST.getCode(), EMPTY_FILE_EXIST.getMessage());
        // 1. 校验文件大小
        long fileSize = multipartFile.getSize();
        final long ONE_M = 1024 * 1024L;
        Assert.isTrue(fileSize < 2 * ONE_M, FILE_TOO_LARGE.getCode(), FILE_TOO_LARGE.getMessage());
        // 2. 校验文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // 允许上传的文件后缀
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "jpg", "png", "webp");
        Assert.isTrue(ALLOW_FORMAT_LIST.contains(fileSuffix), FILE_TYPE_NOT_SUPPORT.getCode(), FILE_TYPE_NOT_SUPPORT.getMessage());
    }

    @Override
    public UserOperatorResponse deletePicture(Long id) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();

        // 判断是否存在
        Picture oldPicture = baseMapper.selectById(id);
        Assert.isTrue(ObjectUtil.isNotNull(oldPicture), PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        userService.checkSelfOrAdmin(oldPicture.getUserId());
        // 操作数据库
        boolean result = baseMapper.deleteById(id) > 0;
        if (result) {
            userOperatorResponse.setSuccess(true);
            return userOperatorResponse;
        }
        userOperatorResponse.setSuccess(false);
        return userOperatorResponse;
    }

    @Override
    public PageResponse<Picture> listPictureByPage(PictureQueryParams params) {
        long current = params.getCurrentPage();
        long size = params.getPageSize();
        Page<Picture> picturePage = new Page<>(current, size);
        LambdaQueryWrapper<Picture> wrapper = Wrappers.lambdaQuery(Picture.class)
                .eq(params.getId() != null, Picture::getId, params.getId())
                .like(StringUtils.isNotBlank(params.getName()), Picture::getName, params.getName())
                .like(params.getCategory() != null, Picture::getCategory, params.getCategory())
                .eq(params.getSpaceId() != null, Picture::getSpaceId, params.getSpaceId())
                .orderByDesc(Picture::getCreateTime);
        // 处理标签查询（JSON数组字段）
        List<String> tags = params.getTags();
        if (CollUtil.isNotEmpty(tags)) {
            wrapper.and(w -> {
                tags.forEach(tag ->
                        w.or().apply("JSON_CONTAINS(tags, '\"{0}\"')", tag)
                );
            });
        }
        picturePage = baseMapper.selectPage(picturePage, wrapper);
        return PageResponse.of(picturePage.getRecords(), (int) picturePage.getTotal(), (int) size,(int) current);
    }

    @Override
    public UserOperatorResponse editPicture(Picture picture) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        // 判断是否存在
        long id = picture.getId();
        Picture oldPicture = baseMapper.selectById(id);
        Assert.isTrue(ObjectUtil.isNotNull(oldPicture), PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        userService.checkSelfOrAdmin(oldPicture.getUserId());
        // 操作数据库
        boolean result = baseMapper.updateById(picture) >0;
        Assert.isTrue(result,() -> new BizException(UPDATE_FAILED));
        userOperatorResponse.setSuccess(true);
        return userOperatorResponse;
    }

    @Override
    public UserOperatorResponse updatePicture(Picture picture) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        // 判断是否存在
        long id = picture.getId();
        Picture oldPicture = baseMapper.selectById(id);
        Assert.isTrue(oldPicture != null, PICTURE_NOT_EXIST.getCode(), PICTURE_NOT_EXIST.getMessage());
        // 操作数据库
        boolean result = baseMapper.updateById(picture) >0;
        Assert.isTrue(result,() -> new BizException(UPDATE_FAILED));
        userOperatorResponse.setSuccess(true);
        return userOperatorResponse;
    }


}




