package com.xyt.init.api.user.response.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 邀请排行信息
 *
 * @author Hollis
 */
@Setter
@Getter
public class InviteRankInfo {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请用户数
     */
    private Integer inviteCount;

    /**
     * 分值
     */
    private Integer inviteScore;
}
