package com.xyt.cloudAtlas.business.domain.exception;


import com.xyt.init.base.exception.BizException;
import com.xyt.init.base.exception.ErrorCode;

/**
 * 认证异常
 *
 * @author Hollis
 */
public class BasicException extends BizException {

    public BasicException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BasicException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public BasicException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public BasicException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public BasicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

}
