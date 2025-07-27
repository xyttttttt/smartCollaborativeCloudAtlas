package com.xyt.init.api.user.constant;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Optional;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRole {


    /**
     * 普通用户
     */
    CUSTOMER,

    /**
     * 超级管理员
     */
    SUPER_ADMIN,

    /**
     * 管理员
     */
    ADMIN;


    // 直接使用枚举名称作为值
    public static UserRole getEnumByValue(String value) {
        if (ObjectUtil.isNull(value)) {
            return null;
        }
        for (UserRole item : UserRole.values()) {
            if (item.name().equals(value)) {
                return item;
            }
        }
        return null;
    }

}
