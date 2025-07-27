package com.xyt.cloudAtlas.business.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.cloudAtlas.business.domain.entity.user.UserInvite;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 16048
* @description 针对表【user_invite(用户邀请表)】的数据库操作Mapper
* @createDate 2025-07-25 16:05:51
* @Entity generator.domain.UserInvite
*/
@Mapper
public interface UserInviteMapper extends BaseMapper<UserInvite> {

}




