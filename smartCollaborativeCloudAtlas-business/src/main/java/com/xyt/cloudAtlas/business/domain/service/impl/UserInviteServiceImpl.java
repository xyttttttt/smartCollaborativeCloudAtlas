package com.xyt.cloudAtlas.business.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.cloudAtlas.business.domain.entity.user.UserInvite;
import com.xyt.cloudAtlas.business.domain.service.UserInviteService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.UserInviteMapper;
import org.springframework.stereotype.Service;

/**
* @author 16048
* @description 针对表【user_invite(用户邀请表)】的数据库操作Service实现
* @createDate 2025-07-25 16:05:51
*/
@Service
public class UserInviteServiceImpl extends ServiceImpl<UserInviteMapper, UserInvite>
    implements UserInviteService {

}




