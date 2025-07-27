package com.xyt.cloudAtlas.business.infrastructure.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * user mapper
 * @author hollis
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User findById(long id);

    /**
     * 根据昵称查询用户
     *
     * @param nickname
     * @return
     */
    User findByNickname(@NotNull String nickname);

    /**
     * 根据邀请码查询用户
     * @param inviteCode
     * @return
     */
    User findByInviteCode(@NotNull String inviteCode);

    /**
     * 根据手机号查询用户
     *
     * @param account
     * @return
     */
    User findByAccount(@NotNull String account);

    /**
     * 根据昵称和密码查询用户
     *
     * @param userAccount
     * @param passwordHash
     * @return
     */
    User findByAccountAndPass(@Param("userAccount") String userAccount, @Param("passwordHash") String passwordHash);

    User findByUserName(@Param("userName") String userName);
}
