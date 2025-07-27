package com.xyt.cloudAtlas.business.domain.exception;


import com.xyt.init.base.exception.ErrorCode;

/**
 * 用户错误码
 *
 * @author hollis
 */
public enum UserErrorCode implements ErrorCode {
    /**
     * 重复电话号码
     */
    DUPLICATE_ACCOUNT("DUPLICATE_ACCOUNT", "重复账号"),

    /**
     * 用户输入的密码不一致
     */
    USER_PASSWORD_NOT_EQUALS("USER_PASSWORD_NOT_EQUALS", "用户两次输入密码不一致"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST("USER_NOT_EXIST", "用户不存在"),
    /**
     * 用户已冻结
     */
    USER_FROZEN("USER_FROZEN", "用户已被冻结"),
    /**
     * 用户已注销
     */
    USER_CANCELLED("USER_CANCELLED", "用户已注销"),
    /**
     * 用户状态不能进行操作
     */
    USER_STATUS_CANT_OPERATE("USER_STATUS_CANT_OPERATE", "用户状态不能进行操作"),

    /**
     * 账号或密码错误
     */
    ACCOUNT_PASSWORD_WRONG("ACCOUNT_PASSWORD_WRONG", "账号或密码错误"),

    /**
     * 用户操作失败
     */
    USER_OPERATE_FAILED("USER_OPERATE_FAILED", "用户操作失败"),

    /**
     * 用户密码校验失败
     */
    USER_PASSWD_CHECK_FAIL("USER_PASSWD_CHECK_FAIL", "用户密码校验失败"),
    /**
     * 用户查询失败
     */
    USER_QUERY_FAIL("USER_QUERY_FAIL", "用户查询失败"),
    /**
     * 用户名已存在
     */
    USER_NAME_EXIST("USER_NAME_EXIST", "用户名已存在"),
    /**
     * 用户上传图片失败
     */
    USER_UPLOAD_PICTURE_FAIL("USER_UPLOAD_PICTURE_FAIL", "用户上传图片失败");

    private String code;

    private String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
