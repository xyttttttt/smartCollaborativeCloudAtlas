package com.xyt.init.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.xyt.init.file.domain.entity.ImageInfo;
import com.xyt.init.file.domain.response.UploadPictureResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * oss 服务
 *
 * @author hollis
 */
@Slf4j
@Setter
public class OssServiceImpl implements FileService {

    private String bucket;

    private String endPoint;

    private String accessKey;

    private String accessSecret;

    @Override
    public UploadPictureResponse upload(String path, InputStream fileStream) throws IOException {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = endPoint;
        // 从环境变量中获取RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。
        String accessKeyId = accessKey;
        String accessKeySecret = accessSecret;
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 填写Bucket名称，例如examplebucket。
        String bucketName = bucket;
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = path;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        boolean uploadRes = false;
        File tempFile = null;
        // 生成上传路径
        String fileExtension = FileUtil.extName(path);

        // 创建临时文件
        tempFile = File.createTempFile("oss-upload-", "." + fileExtension);
        FileUtil.writeFromStream(fileStream, tempFile);

        // 获取图片信息
        ImageInfo imageInfo = getImageInfo(tempFile);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, fileStream);


        // 上传字符串。
        PutObjectResult result = ossClient.putObject(putObjectRequest);

/*            if (StringUtils.isNotBlank(result.getRequestId())) {
                uploadRes = true;
            }*/

        if (tempFile.exists()) {
            tempFile.delete();  // 手动删除
        }
        ossClient.shutdown();
        return buildUploadResult(path, tempFile, imageInfo);

    }


    // 获取图片信息的辅助方法
    private ImageInfo getImageInfo(File imageFile) {
        // 这里可以使用Java的图像处理库如ImageIO或Thumbnailator来获取图片信息
        // 或者调用阿里云的图片处理服务
        // 示例伪代码:
        ImageInfo info = new ImageInfo();
        try {
            BufferedImage image = ImageIO.read(imageFile);
            info.setWidth(image.getWidth());
            info.setHeight(image.getHeight());
            info.setFormat(FileUtil.extName(imageFile.getName()));
            return info;
        } catch (IOException e) {
            throw new RuntimeException("获取图片信息失败", e);
        }
    }

    private UploadPictureResponse buildUploadResult(
            String uploadPath,
            File file,
            ImageInfo imageInfo) {
        UploadPictureResponse response = new UploadPictureResponse();
        int width = imageInfo.getWidth();
        int height = imageInfo.getHeight();

        response.setPicName(FileUtil.mainName(uploadPath));
        response.setPicWidth(width);
        response.setPicHeight(height);
        response.setPicScale(NumberUtil.round(width * 1.0 / height, 2).doubleValue());
        response.setPicFormat(imageInfo.getFormat());
        response.setPicSize(FileUtil.size(file));
        response.setUrl("https://" + bucket + "." + endPoint + "/" + uploadPath);

        return response;
    }


}
