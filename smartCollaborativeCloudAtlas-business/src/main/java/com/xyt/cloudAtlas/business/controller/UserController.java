package com.xyt.cloudAtlas.business.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xyt.cloudAtlas.business.domain.request.user.UserAuthRequest;
import com.xyt.cloudAtlas.business.domain.request.user.UserModifyRequest;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.api.user.response.data.BasicUserInfo;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.convertor.UserConvertor;
import com.xyt.cloudAtlas.business.domain.exception.UserException;
import com.xyt.cloudAtlas.business.domain.params.user.UserModifyNickNameParams;
import com.xyt.cloudAtlas.business.domain.params.user.UserModifyPasswordParams;
import com.xyt.cloudAtlas.business.domain.service.UserService;
import com.xyt.init.file.FileService;
import com.xyt.init.web.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.xyt.cloudAtlas.business.domain.exception.AuthErrorCode.USER_NOT_EXIST;
import static com.xyt.cloudAtlas.business.domain.exception.UserErrorCode.USER_PASSWD_CHECK_FAIL;
import static com.xyt.cloudAtlas.business.domain.exception.UserErrorCode.USER_UPLOAD_PICTURE_FAIL;


/**
 * 用户信息
 *
 * @author
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
@Tag(name = "UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getUserInfo")
    @Operation(summary = "获取用户详情")
    public Result<UserInfo> getUserInfo() {
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));

        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        return Result.success(UserConvertor.INSTANCE.mapToVo(user));
    }

    @GetMapping("/queryUserByName")
    @Operation(summary = "通过昵称获取用户")
    public Result<BasicUserInfo> queryUserByName(String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        return Result.success(UserConvertor.INSTANCE.mapToBasicVo(user));
    }

    @PostMapping("/modifyNickName")
    @Operation(summary = "修改用户昵称")
    public Result<Boolean> modifyNickName(@Valid @RequestBody UserModifyNickNameParams userModifyNickNameParams) {
        String userId = (String) StpUtil.getLoginId();

        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setUserName(userModifyNickNameParams.getUserName());

        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        return Result.success(registerResult);
    }

    @PostMapping("/modifyPassword")
    @Operation(summary = "修改用户密码")
    public Result<Boolean> modifyPassword(@Valid @RequestBody UserModifyPasswordParams userModifyPasswordParams) {
        //查询用户信息
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));

        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        if (!StringUtils.equals(user.getPasswordHash(), DigestUtil.md5Hex(userModifyPasswordParams.getOldPassword()))) {
            throw new UserException(USER_PASSWD_CHECK_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setNewPassword(userModifyPasswordParams.getNewPassword());
        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        return Result.success(registerResult);
    }

    @PostMapping("/modifyProfilePhoto")
    @Operation(summary = "修改用户头像")
    public Result<String> modifyProfilePhoto(@RequestParam("file_data") MultipartFile file) throws Exception {
        String userId = (String) StpUtil.getLoginId();
        String prefix = "https://nfturbo-file.oss-cn-hangzhou.aliyuncs.com/";

        if (null == file) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        String filename = file.getOriginalFilename();
        InputStream fileStream = file.getInputStream();
        String path = "profile/" + userId + "/" + filename;
        var res = fileService.upload(path, fileStream);
        if (StrUtil.isNotBlank(res.getUrl())) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setProfilePhotoUrl(prefix + path);
        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        if (!registerResult) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        return Result.success(prefix + path);
    }

}
