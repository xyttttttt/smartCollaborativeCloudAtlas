package com.xyt.cloudAtlas.business.domain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.cloudAtlas.business.infrastructure.mapper.UserOperationStreamMapper;
import com.xyt.init.api.user.constant.UserOperateTypeEnum;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.UserOperationStream;
import com.xyt.cloudAtlas.business.domain.service.UserOperateStreamService;
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
public class UserOperateStreamServiceImpl extends ServiceImpl<UserOperationStreamMapper, UserOperationStream> implements UserOperateStreamService {


    @Override
    public Long insertStream(User user, UserOperateTypeEnum type) {
        UserOperationStream stream = new UserOperationStream();
        stream.setUserId(user.getId());
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
