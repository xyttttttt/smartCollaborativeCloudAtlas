package com.xyt.cloudAtlas.business.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.convertor.UserConvertor;
import com.xyt.cloudAtlas.business.domain.params.auth.AdminRegisterParams;
import com.xyt.cloudAtlas.business.domain.params.auth.UserQueryParams;
import com.xyt.cloudAtlas.business.domain.response.user.vo.UserVO;
import com.xyt.cloudAtlas.business.domain.service.UserService;
import com.xyt.init.api.user.constant.UserRole;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.base.response.PageResponse;
import com.xyt.init.web.vo.MultiResult;
import com.xyt.init.web.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("admin/user")
@Tag(name = "AdminUserController")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "管理员注册")
    public Result<UserOperatorResponse> adminRegister(AdminRegisterParams registerParams) {

        UserOperatorResponse userOperatorResponse = userService.adminRegister(registerParams.getUserAccount());
        if (userOperatorResponse.getSuccess()){
            return Result.success(userOperatorResponse);
        }
        return Result.error(userOperatorResponse.getResponseCode(), userOperatorResponse.getResponseMessage());
    }


    @PostMapping("/userList")
    @Operation(summary = "用户列表")
    public MultiResult<UserVO> userVoList(UserQueryParams registerParams) {
        PageResponse<User> pageResponse = userService.userVoList(registerParams);
        return MultiResult.successMulti(PageResponse.of(UserConvertor.INSTANCE.mapToUserVo(pageResponse.getDatas())).getDatas(), pageResponse.getTotal(), 1, 10);
    }


    @GetMapping("/freeze")
    @Operation(summary = "冻结用户")
    public Result<UserOperatorResponse> freeze(Long  UserId) {
        UserOperatorResponse freeze = userService.freeze(UserId);
        return Result.success(freeze);
    }

    @GetMapping("/unfreeze")
    @Operation(summary = "冻结用户")
    public Result<UserOperatorResponse> unfreeze(Long  UserId) {
        UserOperatorResponse unfreeze = userService.unfreeze(UserId);
        return Result.success(unfreeze);
    }
}
