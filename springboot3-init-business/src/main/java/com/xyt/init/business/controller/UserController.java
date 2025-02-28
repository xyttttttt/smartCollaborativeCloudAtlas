package com.xyt.init.business.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xyt.init.api.user.request.UserActiveRequest;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.api.user.response.data.BasicUserInfo;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.business.domain.entity.user.User;
import com.xyt.init.business.domain.entity.user.convertor.UserConvertor;
import com.xyt.init.business.domain.exception.UserException;
import com.xyt.init.business.domain.params.user.UserModifyNickNameParams;
import com.xyt.init.business.domain.params.user.UserModifyPasswordParams;
import com.xyt.init.business.domain.request.user.*;
import com.xyt.init.business.domain.service.UserService;
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

import static com.xyt.init.business.domain.exception.AuthErrorCode.USER_NOT_EXIST;
import static com.xyt.init.business.domain.exception.UserErrorCode.USER_PASSWD_CHECK_FAIL;
import static com.xyt.init.business.domain.exception.UserErrorCode.USER_UPLOAD_PICTURE_FAIL;


/**
 * 用户信息
 *
 * @author
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
@Tag(name = "用户控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

//    @Autowired
//    private ChainFacadeService chainFacadeService;

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

    @GetMapping("/queryUserByTel")
    @Operation(summary = "通过手机号获取用户")
    public Result<BasicUserInfo> queryUserByTel(String telephone) {
        User user = userService.findByTelephone(telephone);
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
        userModifyRequest.setNickName(userModifyNickNameParams.getNickName());

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
        if (!res) {
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

    @PostMapping("/auth")
    @Operation(summary = "用户实名认证")
    public Result<Boolean> auth(@Valid @RequestBody UserAuthRequest userAuthParam) {
        String userId = (String) StpUtil.getLoginId();

        //实名认证
        UserAuthRequest userAuthRequest = new UserAuthRequest();
        userAuthRequest.setUserId(Long.valueOf(userId));
        userAuthRequest.setRealName(userAuthParam.getRealName());
        userAuthRequest.setIdCard(userAuthParam.getIdCard());
        UserOperatorResponse authResult = userService.auth(userAuthRequest);
        //实名认证成功，需要进行上链操作
//        if (authResult.getSuccess()) {
//            ChainProcessRequest chainCreateRequest = new ChainProcessRequest();
//            chainCreateRequest.setUserId(userId);
//            String identifier = APP_NAME_UPPER + SEPARATOR + authResult.getUser().getUserRole() + SEPARATOR + authResult.getUser().getUserId();
//            chainCreateRequest.setIdentifier(identifier);
//            ChainProcessResponse<ChainCreateData> chainProcessResponse = chainFacadeService.createAddr(
//                    chainCreateRequest);
//            if (chainProcessResponse.getSuccess()) {
//                //激活账户
//                ChainCreateData chainCreateData = chainProcessResponse.getData();
//                UserActiveRequest userActiveRequest = new UserActiveRequest();
//                userActiveRequest.setUserId(Long.valueOf(userId));
//                userActiveRequest.setBlockChainUrl(chainCreateData.getAccount());
//                userActiveRequest.setBlockChainPlatform(chainCreateData.getPlatform());
//                UserOperatorResponse activeResponse = userService.active(userActiveRequest);
//                if (activeResponse.getSuccess()) {
//                    refreshUserInSession(userId);
//                    return Result.success(true);
//                }
//                return Result.error(activeResponse.getResponseCode(), activeResponse.getResponseMessage());
//            } else {
//                throw new UserException(USER_CREATE_CHAIN_FAIL);
//            }
//        }
        return Result.error(authResult.getResponseCode(), authResult.getResponseMessage());
    }

    private void refreshUserInSession(String userId) {
        User user = userService.getById(userId);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
    }
}
