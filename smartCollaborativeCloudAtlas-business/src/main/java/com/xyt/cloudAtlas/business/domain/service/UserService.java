package com.xyt.cloudAtlas.business.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.init.api.user.request.UserActiveRequest;
import com.xyt.init.api.user.request.UserQueryRequest;
import com.xyt.init.api.user.request.UserRegisterRequest;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.api.user.response.UserQueryResponse;
import com.xyt.init.api.user.response.data.InviteRankInfo;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.base.response.PageResponse;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.request.user.UserAuthRequest;
import com.xyt.cloudAtlas.business.domain.request.user.UserModifyRequest;

import java.util.List;

public interface UserService extends IService<User> {


    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest);


    public UserOperatorResponse registerAdmin(String telephone, String password);

    public UserOperatorResponse auth(UserAuthRequest userAuthRequest);

    public UserOperatorResponse active(UserActiveRequest userActiveRequest);

    public UserOperatorResponse freeze(Long userId);

    public UserOperatorResponse unfreeze(Long userId);

    public PageResponse<User> pageQueryByState(String keyWord, String state, int currentPage, int pageSize);

    public User findByTelephoneAndPass(String telephone, String password);

    public User findByTelephone(String telephone);

    public User findById(Long userId);

    public UserOperatorResponse modify(UserModifyRequest userModifyRequest);

    public Integer getInviteRank(String userId);

    public PageResponse<User> getUsersByInviterId(String inviterId, int currentPage, int pageSize);

    public List<InviteRankInfo> getTopN(Integer topN);

    public boolean nickNameExist(String nickName);

    public boolean inviteCodeExist(String inviteCode);

    UserQueryResponse<UserInfo> queryUser(UserQueryRequest userQueryRequest);
}
