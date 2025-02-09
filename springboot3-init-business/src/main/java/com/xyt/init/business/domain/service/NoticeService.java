package com.xyt.init.business.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.init.api.notice.response.NoticeResponse;
import com.xyt.init.business.domain.entity.notice.Notice;


public interface NoticeService extends IService<Notice> {


    public Page<Notice> pageQueryForRetry(int currentPage, int pageSize);


    public Notice saveCaptcha(String telephone, String captcha);

    NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
