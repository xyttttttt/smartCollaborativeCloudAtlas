package com.xyt.init.sms;

import com.xyt.init.lock.DistributeLock;
import com.xyt.init.sms.response.SmsSendResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock短信服务
 *
 * @author hollis
 */
@Slf4j
@Setter
public class MockSmsServiceImpl implements SmsService {

    private static Logger logger = LoggerFactory.getLogger(MockSmsServiceImpl.class);

    @DistributeLock(scene = "SEND_SMS", keyExpression = "#phoneNumber")
    @Override
    public SmsSendResponse sendMsg(String phoneNumber, String code) {
        SmsSendResponse smsSendResponse = new SmsSendResponse();
        smsSendResponse.setSuccess(true);
        return smsSendResponse;
    }
}
