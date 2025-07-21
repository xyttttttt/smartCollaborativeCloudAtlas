package com.xyt.init.api.notice.service;


import com.xyt.init.api.notice.response.NoticeResponse;

/**
 * @author Hollis
 */
public interface NoticeFacadeService {
    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    public NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
