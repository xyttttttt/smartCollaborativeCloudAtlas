package com.xyt.cloudAtlas.business.domain.exception;

import com.xyt.init.base.exception.BizException;
import com.xyt.init.base.exception.ErrorCode;

public class PictureException extends BizException {
    public PictureException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PictureException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PictureException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public PictureException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public PictureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
