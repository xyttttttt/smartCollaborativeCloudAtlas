package com.xyt.cloudAtlas.business.utils;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

public class ClientUtil {

    /**
     * 获取客户端真实IP地址
     * @return 客户端IP
     */
    public static String getClientIp() {
        HttpServletRequest request = SpringMVCUtil.getRequest();
        if (request == null) {
            return null;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况（如代理）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
    public static String getUserAgent(){
        // 获取User-Agent
        String userAgent = SpringMVCUtil.getRequest().getHeader("User-Agent");
        return userAgent;
    }
}
