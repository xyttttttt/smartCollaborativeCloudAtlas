package com.xyt.init.sms;

import com.xyt.init.sms.response.SmsSendResponse;

/**
 * 短信服务
 *
 * @author hollis
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public SmsSendResponse sendMsg(String phoneNumber, String code);
}
