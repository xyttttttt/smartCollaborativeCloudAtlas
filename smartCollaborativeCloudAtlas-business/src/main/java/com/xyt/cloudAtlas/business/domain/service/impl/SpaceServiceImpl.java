package com.xyt.cloudAtlas.business.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xyt.cloudAtlas.business.domain.entity.space.Space;
import com.xyt.cloudAtlas.business.domain.service.SpaceService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.SpaceMapper;
import org.springframework.stereotype.Service;

/**
* @author 16048
* @description 针对表【space(空间)】的数据库操作Service实现
* @createDate 2025-07-25 16:05:46
*/
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
    implements SpaceService {

}




