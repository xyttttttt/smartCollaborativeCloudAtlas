package com.xyt.cloudAtlas.business.domain.entity.picture.convertor;

import com.xyt.cloudAtlas.business.domain.entity.picture.Picture;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.convertor.UserConvertor;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureEditParams;
import com.xyt.cloudAtlas.business.domain.params.picture.PictureUpdateParams;
import com.xyt.cloudAtlas.business.domain.response.priture.vo.PictureVO;
import com.xyt.init.api.user.response.data.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PictureConvertor {
    PictureConvertor INSTANCE = Mappers.getMapper(PictureConvertor.class);

    /**
     * 转换为vo
     *
     * @param request
     * @return
     */
    @Mapping(target = "id", source = "request.id")
    public PictureVO mapToVo(Picture request);


    /**
     * 转换为VO
     *
     * @param request
     * @return
     */
    @Mapping(target = "id", source = "request.id")
    public List<PictureVO> mapToVoList(List<Picture> request);


    /**
     * 转换为实体
     *
     * @param request
     * @return
     */
    @Mapping(target = "id", source = "request.id")
    public Picture mapToEntity(PictureVO request);

    @Mapping(target = "tags", expression = "java(MappingUtils.listToJson(pictureEditParams.getTags()))")
    Picture editMapToEntity(PictureEditParams pictureEditParams);

    @Mapping(target = "tags", expression = "java(MappingUtils.listToJson(pictureEditParams.getTags()))")
    Picture updateMapToEntity(PictureUpdateParams pictureUpdateParams);
}
