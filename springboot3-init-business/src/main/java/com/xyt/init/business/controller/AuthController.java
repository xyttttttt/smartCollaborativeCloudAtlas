package com.xyt.init.business.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.xyt.init.api.notice.response.NoticeResponse;
import com.xyt.init.api.user.request.UserQueryRequest;
import com.xyt.init.api.user.request.UserRegisterRequest;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.api.user.response.UserQueryResponse;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.base.validator.IsMobile;
import com.xyt.init.business.domain.constant.LoginType;
import com.xyt.init.business.domain.exception.AuthException;
import com.xyt.init.business.domain.params.auth.LoginParams;
import com.xyt.init.business.domain.params.auth.RegisterParams;
import com.xyt.init.business.domain.response.user.vo.LoginVO;
import com.xyt.init.business.domain.service.NoticeService;
import com.xyt.init.business.domain.service.UserService;
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

import static com.xyt.init.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static com.xyt.init.business.domain.exception.AuthErrorCode.VERIFICATION_CODE_WRONG;

/**
 * 认证相关接口
 *
 * @author Hollis
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
@Tag(name = "用户认证控制器")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;


    private static final String ROOT_CAPTCHA = "8888";

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

        //验证码校验
        String cachedCode = redisTemplate.opsForValue().get(CAPTCHA_KEY_PREFIX + registerParams.getTelephone());
        if (!StringUtils.equalsIgnoreCase(cachedCode, registerParams.getCaptcha())) {
            throw new AuthException(VERIFICATION_CODE_WRONG);
        }

        //注册
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setTelephone(registerParams.getTelephone());
        userRegisterRequest.setInviteCode(registerParams.getInviteCode());
        userRegisterRequest.setPassword(registerParams.getPassword());

        UserOperatorResponse registerResult = userService.register(userRegisterRequest);
        if(registerResult.getSuccess()){
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

        UserQueryRequest userQueryRequest = new UserQueryRequest(loginParams.getTelephone());
        if (loginParams.getLoginType() == null || loginParams.getLoginType().equals(LoginType.PASSWORD)){
            userQueryRequest = new UserQueryRequest(loginParams.getTelephone(), loginParams.getPassword());
        }
        // fixme 为了方便，暂时直接跳过
        else if (!ROOT_CAPTCHA.equals(loginParams.getCaptcha())){
            String cachedCode = redisTemplate.opsForValue().get(CAPTCHA_KEY_PREFIX + loginParams.getTelephone());
            if (!StringUtils.equalsIgnoreCase(cachedCode, loginParams.getCaptcha())) {
                throw new AuthException(VERIFICATION_CODE_WRONG);
            }
        }
        //判断是注册还是登陆
        //查询用户信息
        UserQueryResponse<UserInfo> userQueryResponse = userService.queryUser(userQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();
        if (userInfo == null && loginParams.getLoginType().equals(LoginType.TELEPHONE)) {
            //查询到的用户为空并且当前登录方式为验证码方式 需要注册
            UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
            userRegisterRequest.setTelephone(loginParams.getTelephone());
            userRegisterRequest.setInviteCode(loginParams.getInviteCode());
            userRegisterRequest.setPassword(loginParams.getPassword());
            UserOperatorResponse response = userService.register(userRegisterRequest);
            if (response.getSuccess()) {
                userQueryResponse = userService.queryUser(userQueryRequest);
                userInfo = userQueryResponse.getData();
                StpUtil.login(userInfo.getUserId(), new SaLoginModel().setIsLastingCookie(loginParams.getRememberMe())
                        .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
                StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
                LoginVO loginVO = new LoginVO(userInfo);
                return Result.success(loginVO);
            }

            return Result.error(response.getResponseCode(), response.getResponseMessage());
        } // todo 密码登录错误直接返回错误信息
        else if (userInfo == null && loginParams.getLoginType().equals(LoginType.PASSWORD)) {
            return Result.error("401","用户未注册或密码错误，忘记可取用户中心修改密码");
        } else {
            //登录
            StpUtil.login(userInfo.getUserId(), new SaLoginModel().setIsLastingCookie(loginParams.getRememberMe())
                    .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
            StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
            LoginVO loginVO = new LoginVO(userInfo);
            return Result.success(loginVO);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }

}
