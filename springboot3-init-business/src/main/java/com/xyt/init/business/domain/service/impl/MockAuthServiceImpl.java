package com.xyt.init.business.domain.service.impl;

import com.xyt.init.business.domain.service.AuthService;

/**
 * Mock的认证服务
 *
 * @author hollis
 */
public class MockAuthServiceImpl implements AuthService {
    @Override
    public boolean checkAuth(String realName, String idCard) {
        return true;
    }
}
