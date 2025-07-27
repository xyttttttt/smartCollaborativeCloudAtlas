package com.xyt.init.file;

import com.xyt.init.file.domain.response.UploadPictureResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件 服务
 *
 * @author hollis
 */
public interface FileService {

    /**
     * 文件上传
     * @param path
     * @param fileStream
     * @return
     */
    public UploadPictureResponse upload(String path, InputStream fileStream) throws IOException;

}
