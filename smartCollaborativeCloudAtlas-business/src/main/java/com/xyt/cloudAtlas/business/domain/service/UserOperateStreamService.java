package com.xyt.cloudAtlas.business.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.init.api.user.constant.UserOperateTypeEnum;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.UserOperationStream;

public interface UserOperateStreamService extends IService<UserOperationStream> {

    public Long insertStream(User user, UserOperateTypeEnum type);
}
