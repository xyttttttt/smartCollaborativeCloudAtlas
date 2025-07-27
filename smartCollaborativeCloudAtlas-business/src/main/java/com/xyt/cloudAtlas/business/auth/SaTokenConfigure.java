package com.xyt.cloudAtlas.business.auth;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

import com.xyt.init.api.user.constant.UserPermission;
import com.xyt.init.api.user.constant.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa-token的全局配置
 *
 * @author Hollis
 */
//@Configuration
//@Slf4j
//public class SaTokenConfigure {
//
//    @Bean
//    public SaServletFilter getSaServletFilter() {
//        return new SaServletFilter()
//                // 拦截地址
//                .addInclude("/**")
//                // 放行所有 OPTIONS 请求（避免预检失败）
//                .setBeforeAuth(obj -> {
//                    if (SaHolder.getRequest().getMethod().equals("OPTIONS")) {
//                        SaRouter.back();
//                    }
//                })
//                // 开放地址
//                .addExclude("/favicon.ico")
//                // 鉴权方法：每次访问进入
//                .setAuth(obj -> {
//                    SaRouter.match("/**")
//                            .notMatch("/auth/register",
//                            "/user/**",
//                            "/auth/login",
//                            "/auth/register",
//                            "/auth/logout",
//                            "/doc.html","/v3/api-docs/**","/swagger-ui.html",
//                            "/webjars/**","/favicon.ico/")
//                            .check(r -> StpUtil.checkLogin());      // 排除掉的 path 列表，可以写多个
//
//                    // 权限认证 -- 不同模块, 校验不同权限
//                    SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr(UserRole.ADMIN.name(),UserRole.SUPER_ADMIN.name()));
////                    SaRouter.match("/user/**", r -> StpUtil.checkPermissionOr(UserPermission.BASIC.name(), UserPermission.FROZEN.name()));
//                })
//                // 异常处理方法：每次setAuth函数出现异常时进入
//                .setError(this::getSaResult);
//    }
//
//    private SaResult getSaResult(Throwable throwable) {
//        switch (throwable) {
//            case NotLoginException notLoginException:
//                log.error("请先登录");
//                return SaResult.error("请先登录");
//            case NotRoleException notRoleException:
//                if (UserRole.ADMIN.name().equals(notRoleException.getRole())) {
//                    log.error("请勿越权使用！");
//                    return SaResult.error("请勿越权使用！");
//                }
//                log.error("您无权限进行此操作！");
//                return SaResult.error("您无权限进行此操作！");
//            case NotPermissionException notPermissionException:
//                if (UserPermission.AUTH.name().equals(notPermissionException.getPermission())) {
//                    log.error("请先完成实名认证！");
//                    return SaResult.error("请先完成实名认证！");
//                }
//                log.error("您无权限进行此操作！");
//                return SaResult.error("您无权限进行此操作！");
//            default:
//                return SaResult.error(throwable.getMessage());
//        }
//    }
//}
