package com.xyt.init.business.domain.service;

/**
 * 认证服务
 *
 * @author hollis
 */
public interface AuthService {
    /**
     * 校验认证信息
     *
     * @param realName
     * @param idCard
     * @return
     */
    public boolean checkAuth(String realName, String idCard);
}
