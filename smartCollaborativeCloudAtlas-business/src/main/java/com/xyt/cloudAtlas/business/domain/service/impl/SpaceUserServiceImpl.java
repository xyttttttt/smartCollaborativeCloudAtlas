package com.xyt.cloudAtlas.business.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.cloudAtlas.business.domain.entity.space.SpaceUser;
import com.xyt.cloudAtlas.business.domain.service.SpaceUserService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.SpaceUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 16048
* @description 针对表【space_user(空间用户关联)】的数据库操作Service实现
* @createDate 2025-07-25 16:05:48
*/
@Service
public class SpaceUserServiceImpl extends ServiceImpl<SpaceUserMapper, SpaceUser>
    implements SpaceUserService {

}




