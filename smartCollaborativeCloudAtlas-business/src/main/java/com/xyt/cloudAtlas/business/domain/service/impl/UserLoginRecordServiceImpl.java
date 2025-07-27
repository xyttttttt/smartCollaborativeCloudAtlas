package com.xyt.cloudAtlas.business.domain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.cloudAtlas.business.domain.entity.user.UserLoginRecord;
import com.xyt.cloudAtlas.business.domain.service.UserLoginRecordService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.UserLoginRecordMapper;
import com.xyt.cloudAtlas.business.utils.ClientUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 16048
* @description 针对表【user_login_record(用户登录记录表)】的数据库操作Service实现
* @createDate 2025-07-25 16:05:53
*/
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord>
    implements UserLoginRecordService {

    @Override
    public void insertLoginRecord(Long userId) {
        this.save(UserLoginRecord.builder()
                .userId(userId)
                .ip(ClientUtil.getClientIp())
                .userAgent(ClientUtil.getUserAgent())
                .loginTime(new Date())
                .build());
    }
}




