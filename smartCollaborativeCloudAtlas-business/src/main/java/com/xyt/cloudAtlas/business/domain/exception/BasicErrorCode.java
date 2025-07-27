package com.xyt.cloudAtlas.business.domain.exception;


import com.xyt.init.base.exception.ErrorCode;

/**
 * 认证错误码
 *
 * @author Hollis
 */
public enum BasicErrorCode implements ErrorCode {

    /**
     * 参数异常
     */
    PARAM_ERROR("PARAM_ERROR", "参数异常");



    private String code;

    private String message;

    BasicErrorCode(String code, String message) {
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
