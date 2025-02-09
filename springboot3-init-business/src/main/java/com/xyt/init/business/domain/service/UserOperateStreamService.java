package com.xyt.init.business.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.init.api.user.constant.UserOperateTypeEnum;
import com.xyt.init.business.domain.entity.user.User;
import com.xyt.init.business.domain.entity.user.UserOperateStream;

public interface UserOperateStreamService extends IService<UserOperateStream> {

    public Long insertStream(User user, UserOperateTypeEnum type);
}
