package com.xyt.cloudAtlas.business.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.cloudAtlas.business.domain.entity.user.UserLoginRecord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 16048
* @description 针对表【user_login_record(用户登录记录表)】的数据库操作Mapper
* @createDate 2025-07-25 16:05:53
* @Entity generator.domain.UserLoginRecord
*/
@Mapper
public interface UserLoginRecordMapper extends BaseMapper<UserLoginRecord> {

}




