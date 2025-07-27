package com.xyt.cloudAtlas.business.infrastructure.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.cloudAtlas.business.domain.entity.picture.Picture;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 16048
* @description 针对表【picture(图片)】的数据库操作Mapper
* @createDate 2025-07-25 16:05:43
* @Entity generator.domain.Picture
*/
@Mapper
public interface PictureMapper extends BaseMapper<Picture> {

}




