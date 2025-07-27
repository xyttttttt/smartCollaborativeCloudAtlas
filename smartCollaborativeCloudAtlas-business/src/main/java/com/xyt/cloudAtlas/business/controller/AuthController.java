package com.xyt.cloudAtlas.business.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.xyt.cloudAtlas.business.domain.constant.LoginType;
import com.xyt.cloudAtlas.business.domain.exception.AuthErrorCode;
import com.xyt.cloudAtlas.business.domain.exception.AuthException;
import com.xyt.cloudAtlas.business.domain.exception.UserErrorCode;
import com.xyt.cloudAtlas.business.domain.exception.UserException;
import com.xyt.cloudAtlas.business.domain.params.auth.LoginParams;
import com.xyt.cloudAtlas.business.domain.response.user.vo.LoginVO;
import com.xyt.cloudAtlas.business.domain.service.NoticeService;
import com.xyt.cloudAtlas.business.domain.service.UserLoginRecordService;
import com.xyt.cloudAtlas.business.domain.service.UserService;
import com.xyt.init.api.notice.response.NoticeResponse;
import com.xyt.init.api.user.constant.UserStateEnum;
import com.xyt.init.api.user.request.UserQueryRequest;
import com.xyt.init.api.user.request.UserRegisterRequest;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.api.user.response.UserQueryResponse;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.base.validator.IsMobile;
import com.xyt.cloudAtlas.business.domain.params.auth.RegisterParams;
import com.xyt.init.web.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.xyt.cloudAtlas.business.domain.exception.UserErrorCode.*;
import static com.xyt.init.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;

/**
 * 认证相关接口
 *
 * @author Hollis
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
@Tag(name = "AuthController")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private NoticeService noticeService;

    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    @GetMapping("/sendCaptcha")
    @Operation(summary = "获取验证码")
    public Result<Boolean> sendCaptcha(@IsMobile String telephone) {
        NoticeResponse noticeResponse = noticeService.generateAndSendSmsCaptcha(telephone);
        return Result.success(noticeResponse.getSuccess());
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Boolean> register(@Valid @RequestBody RegisterParams registerParams) {
        //注册
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserAccount(registerParams.getUserAccount());
        userRegisterRequest.setInviteCode(registerParams.getInviteCode());
        userRegisterRequest.setPassword(registerParams.getPassword());
        userRegisterRequest.setCheckPassword(registerParams.getCheckPassword());

        UserOperatorResponse registerResult = userService.register(userRegisterRequest);
        if (registerResult.getSuccess()) {
            return Result.success(true);
        }
        return Result.error(registerResult.getResponseCode(), registerResult.getResponseMessage());
    }

    /**
     * 登录方法
     *
     * @param loginParams 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginVO> login(@Valid @RequestBody LoginParams loginParams) {


        UserQueryRequest userQueryRequest = new UserQueryRequest(loginParams.getUserAccount(), loginParams.getPassword());

        //查询用户信息
        UserQueryResponse<UserInfo> userQueryResponse = userService.queryUser(userQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();
        //判断用户状态
        Assert.isTrue(ObjectUtil.isNotNull(userInfo), () -> new UserException(ACCOUNT_PASSWORD_WRONG));
        Assert.isTrue(userInfo.getState() != UserStateEnum.FROZEN.getValue(), () -> new UserException(USER_FROZEN));
        Assert.isTrue(userInfo.getState() != UserStateEnum.CANCELLED.getValue(), () -> new UserException(USER_CANCELLED));
        //插入登录记录
        userLoginRecordService.insertLoginRecord(userInfo.getUserId());
        //登录
        StpUtil.login(userInfo.getUserId(), new SaLoginModel().setIsLastingCookie(loginParams.getRememberMe())
                .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
        LoginVO loginVO = new LoginVO(userInfo);
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }

}
