package com.xyt.cloudAtlas.business.domain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.init.api.user.constant.UserOperateTypeEnum;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.UserOperateStream;
import com.xyt.cloudAtlas.business.domain.service.UserOperateStreamService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.UserOperateStreamMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户操作流水表 服务类
 * </p>
 *
 * @author wswyb001
 * @since 2024-01-13
 */
@Service
public class UserOperateStreamServiceImpl extends ServiceImpl<UserOperateStreamMapper, UserOperateStream> implements UserOperateStreamService {


    @Override
    public Long insertStream(User user, UserOperateTypeEnum type) {
        UserOperateStream stream = new UserOperateStream();
        stream.setUserId(String.valueOf(user.getId()));
        stream.setOperateTime(new Date());
        stream.setType(type.name());
        stream.setParam(JSON.toJSONString(user));
        boolean result = save(stream);
        if (result) {
            return stream.getId();
        }
        return null;
    }
}
