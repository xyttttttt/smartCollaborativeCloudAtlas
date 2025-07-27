package com.xyt.cloudAtlas.business.domain.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.cloudAtlas.business.domain.entity.user.UserLoginRecord;

/**
* @author 16048
* @description 针对表【user_login_record(用户登录记录表)】的数据库操作Service
* @createDate 2025-07-25 16:05:53
*/
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    void insertLoginRecord(Long userId);
}
