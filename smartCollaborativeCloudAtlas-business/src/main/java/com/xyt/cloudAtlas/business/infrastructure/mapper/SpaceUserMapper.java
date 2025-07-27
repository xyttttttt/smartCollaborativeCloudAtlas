package com.xyt.cloudAtlas.business.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.cloudAtlas.business.domain.entity.space.SpaceUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 16048
* @description 针对表【space_user(空间用户关联)】的数据库操作Mapper
* @createDate 2025-07-25 16:05:48
* @Entity generator.domain.SpaceUser
*/
@Mapper
public interface SpaceUserMapper extends BaseMapper<SpaceUser> {

}




