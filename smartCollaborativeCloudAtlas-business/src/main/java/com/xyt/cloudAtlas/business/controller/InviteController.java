package com.xyt.cloudAtlas.business.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.convertor.UserConvertor;
import com.xyt.cloudAtlas.business.domain.service.UserService;
import com.xyt.init.api.user.response.data.InviteRankInfo;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.base.response.PageResponse;
import com.xyt.init.web.vo.MultiResult;
import com.xyt.init.web.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 邀请
 *
 * @author
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user/invite")
@Tag(name = "InviteController")
public class InviteController {

    @Autowired
    private UserService userService;

    @GetMapping("/getTopN")
    @Operation(summary = "获取用户积分排行")
    public MultiResult<InviteRankInfo> getTopN(@Max(100) Integer topN) {
        if (topN == null) {
            topN = 100;
        }

        List<InviteRankInfo> inviteRankInfos = userService.getTopN(topN);
        return MultiResult.successMulti(inviteRankInfos, topN, 1, 10);
    }

    @GetMapping("/getMyRank")
    @Operation(summary = "获取我的积分")
    public Result<Integer> getMyRank() {
        String userId = (String) StpUtil.getLoginId();
        Integer rank = userService.getInviteRank(userId);
        return Result.success(rank);
    }

    @GetMapping("/getInviteList")
    @Operation(summary = "获取受邀用户列表")
    public MultiResult<UserInfo> getInviteList(int currentPage) {
        String userId = (String) StpUtil.getLoginId();

        PageResponse<User> pageResponse = userService.getUsersByInviterId(userId,currentPage,20);

        return MultiResult.successMulti(UserConvertor.INSTANCE.mapToVo(pageResponse.getDatas()), pageResponse.getTotal(), currentPage, 20);
    }
}
