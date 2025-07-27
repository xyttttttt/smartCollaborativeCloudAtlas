import cn.hutool.core.util.ObjectUtil;
import com.xyt.init.file.OssServiceImpl;
import com.xyt.init.file.config.OssConfiguration;
import com.xyt.init.file.domain.response.UploadPictureResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OssConfiguration.class})
@ActiveProfiles("test")
public class OssServiceTest {

    @Autowired
    private OssServiceImpl ossService;

    @Test
    @Ignore
    public void testUploadFile() throws IOException {
        // 填写字符串。
        String content = "Hello OSS，你好世界";
        //https://nfturbo-file.oss-cn-hangzhou.aliyuncs.com/img/test.txt
        UploadPictureResponse uploadPictureResponse = ossService.upload("img/123.txt", new ByteArrayInputStream(content.getBytes()));
        Assert.assertTrue(ObjectUtil.isNull(uploadPictureResponse));

    }
}
