package com.xyt.cloudAtlas.business.domain.exception;


import com.xyt.init.base.exception.ErrorCode;

/**
 * 用户错误码
 *
 * @author hollis
 */
public enum PictureErrorCode implements ErrorCode {
    /**
     * 上传文件为空
     */
    EMPTY_FILE_EXIST("EMPTY_FILE_EXIST", "上传文件为空"),

    /**
     * 文件大小不能超过2M
     */
    FILE_TOO_LARGE("FILE_TOO_LARGE", "文件大小不能超过2M"),
    /**
     * 文件类型错误
     */
    FILE_TYPE_NOT_SUPPORT("FILE_TYPE_NOT_SUPPORT", "文件类型错误"),

    /**
     * 文件保存失败
     */
    PICTURE_SAVE_FAILED("PICTURE_SAVE_FAILED", "文件保存失败"),

    /**
     *
     */
    PICTURE_NOT_EXIST("PICTURE_NOT_EXIST", "图片不存在");


    private String code;

    private String message;

    PictureErrorCode(String code, String message) {
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
