package com.xyt.init.business.infrastructure.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.init.business.domain.entity.notice.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 链操作 Mapper 接口
 * </p>
 *
 * @author wswyb001
 * @since 2024-01-19
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

}
