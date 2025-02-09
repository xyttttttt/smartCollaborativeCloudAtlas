package com.xyt.init.business.domain.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.init.api.notice.response.NoticeResponse;
import com.xyt.init.base.exception.BizException;
import com.xyt.init.base.exception.SystemException;
import com.xyt.init.business.domain.constant.NoticeState;
import com.xyt.init.business.domain.constant.NoticeType;
import com.xyt.init.business.domain.entity.notice.Notice;
import com.xyt.init.business.domain.service.NoticeService;
import com.xyt.init.business.infrastructure.mapper.NoticeMapper;
import com.xyt.init.limiter.SlidingWindowRateLimiter;
import com.xyt.init.sms.SmsService;
import com.xyt.init.sms.response.SmsSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.xyt.init.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static com.xyt.init.base.exception.BizErrorCode.NOTICE_SAVE_FAILED;
import static com.xyt.init.base.exception.BizErrorCode.SEND_NOTICE_DUPLICATED;


/**
 * 通知服务
 *
 * @author Hollis
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {


    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsService smsService;

    private static final String SMS_NOTICE_TITLE = "验证码";

    public Page<Notice> pageQueryForRetry(int currentPage, int pageSize) {
        Page<Notice> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.in("state", NoticeState.INIT.name(),NoticeState.FAILED);

        wrapper.orderBy(true, true, "gmt_create");

        return this.page(page, wrapper);
    }


    public Notice saveCaptcha(String telephone, String captcha) {
        Notice notice = Notice.builder()
                .noticeTitle(SMS_NOTICE_TITLE)
                .noticeContent(captcha)
                .noticeType(NoticeType.SMS)
                .targetAddress(telephone)
                .state(NoticeState.INIT)
                .build();

        Boolean saveResult = this.save(notice);

        if (!saveResult) {
            throw new BizException(NOTICE_SAVE_FAILED);
        }

        return notice;
    }

    @Override
    public NoticeResponse generateAndSendSmsCaptcha(String telephone) {
        Boolean access = slidingWindowRateLimiter.tryAcquire(telephone, 1, 60);

        if (!access) {
            throw new SystemException(SEND_NOTICE_DUPLICATED);
        }

        // 生成验证码
        String captcha = RandomUtil.randomNumbers(4);

        // 验证码存入Redis
        redisTemplate.opsForValue().set(CAPTCHA_KEY_PREFIX + telephone, captcha, 5, TimeUnit.MINUTES);

        Notice notice = this.saveCaptcha(telephone, captcha);

        Thread.ofVirtual().start(() -> {
            SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());
            if (result.getSuccess()) {
                notice.setState(NoticeState.SUCCESS);
                notice.setSendSuccessTime(new Date());
                this.updateById(notice);
            } else {
                notice.setState(NoticeState.FAILED);
                notice.addExtendInfo("executeResult", JSON.toJSONString(result));
                this.updateById(notice);
            }
        });

        return new NoticeResponse.Builder().setSuccess(true).build();
    }
}
