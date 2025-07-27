package com.xyt.cloudAtlas.business.auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.xyt.init.api.user.constant.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;



public class SaTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求（避免 CORS 预检失败）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 开放路径（不需要登录的接口）
        String path = request.getRequestURI();
        if (isExcludePath(path)) {
            return true;
        }

        // 检查登录状态
        StpUtil.checkLogin();

        // 角色权限校验
        if (path.startsWith("/admin/")) {
            StpUtil.checkRoleOr(UserRole.ADMIN.name(), UserRole.SUPER_ADMIN.name());
        }

        return true;
    }

    // 判断是否是不需要鉴权的路径
    private boolean isExcludePath(String path) {
        return path.equals("/favicon.ico")
                || path.startsWith("/auth/")
                || path.startsWith("/user/")
                || path.startsWith("/doc.html")
                || path.startsWith("/v3/api-docs/")
                || path.startsWith("/swagger-ui.html")
                || path.startsWith("/webjars/");
    }
}