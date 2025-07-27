package com.xyt.init.file;

import com.xyt.init.file.domain.response.UploadPictureResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * oss 服务
 *
 * @author hollis
 */
@Slf4j
@Setter
public class MockFileServiceImpl implements FileService {


    @Override
    public UploadPictureResponse upload(String path, InputStream fileStream) {
        return null;
    }

}
