package com.xyt.cloudAtlas.business.domain.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.cloudAtlas.business.domain.params.auth.UserQueryParams;
import com.xyt.init.api.user.constant.UserOperateTypeEnum;
import com.xyt.init.api.user.constant.UserStateEnum;
import com.xyt.init.api.user.request.UserQueryRequest;
import com.xyt.init.api.user.request.UserRegisterRequest;
import com.xyt.init.api.user.request.condition.UserIdQueryCondition;
import com.xyt.init.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import com.xyt.init.api.user.request.condition.UserPhoneQueryCondition;
import com.xyt.init.api.user.response.UserOperatorResponse;
import com.xyt.init.api.user.response.UserQueryResponse;
import com.xyt.init.api.user.response.data.InviteRankInfo;
import com.xyt.init.api.user.response.data.UserInfo;
import com.xyt.init.base.exception.BizException;
import com.xyt.init.base.exception.RepoErrorCode;
import com.xyt.init.base.response.PageResponse;
import com.xyt.cloudAtlas.business.domain.entity.user.User;
import com.xyt.cloudAtlas.business.domain.entity.user.convertor.UserConvertor;
import com.xyt.cloudAtlas.business.domain.exception.UserErrorCode;
import com.xyt.cloudAtlas.business.domain.exception.UserException;
import com.xyt.cloudAtlas.business.domain.request.user.UserModifyRequest;
import com.xyt.cloudAtlas.business.domain.service.AuthService;
import com.xyt.cloudAtlas.business.domain.service.UserOperateStreamService;
import com.xyt.cloudAtlas.business.domain.service.UserService;
import com.xyt.cloudAtlas.business.infrastructure.mapper.UserMapper;
import com.xyt.init.lock.DistributeLock;
import com.xyt.init.web.vo.Result;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xyt.cloudAtlas.business.domain.exception.AuthErrorCode.AUTH_ERROR_CODE;
import static com.xyt.cloudAtlas.business.domain.exception.UserErrorCode.*;


/**
 * 用户服务
 *
 * @author hollis
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements InitializingBean, UserService {

    private static final String DEFAULT_NICK_NAME_PREFIX = "用户_";
    private static final String DEFAULT_ADMIN_NICK_NAME_PREFIX = "管理_";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserOperateStreamService userOperateStreamService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedissonClient redissonClient;

    private RBloomFilter<String> nickNameBloomFilter;

    private RBloomFilter<String> inviteCodeBloomFilter;

    private RScoredSortedSet<String> inviteRank;

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, User> idUserCache;

    @Autowired
    private UserCacheDelayDeleteService userCacheDelayDeleteService;

    @PostConstruct
    public void init() {
        QuickConfig idQc = QuickConfig.newBuilder(":user:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(2))
                .syncLocal(true)
                .build();
        idUserCache = cacheManager.getOrCreateCache(idQc);
    }

    @DistributeLock(keyExpression = "#userAccount", scene = "USER_REGISTER")
    @Transactional
    @Override
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        String inviteCode = userRegisterRequest.getInviteCode();
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Assert.isTrue(password.equals(checkPassword), UserErrorCode.USER_PASSWORD_NOT_EQUALS.getCode());

        String defaultNickName;
        String randomString;
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            //前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_NICK_NAME_PREFIX + randomString + userAccount.substring(userAccount.length()-4);
        } while (nickNameExist(defaultNickName) || inviteCodeExist(randomString));

        String inviterId = null;
        if (StringUtils.isNotBlank(inviteCode)) {
            User inviter = userMapper.findByInviteCode(inviteCode);
            if (inviter != null) {
                inviterId = inviter.getId().toString();
                //todo 插入用户邀请
            }
        }

        User user = register(userAccount, defaultNickName, password, randomString, inviterId);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());

        addUserName(defaultNickName);
        addInviteCode(randomString);
        updateInviteRank(inviterId);
        updateUserCache(user.getId().toString(), user);

        //加入流水
        long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    /**
     * 管理员注册
     *
     * @param telephone
     * @param password
     * @return
     */
    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    @Transactional
    @Override
    public UserOperatorResponse registerAdmin(String telephone, String password) {
        User user = registerAdmin(telephone, telephone, password);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());
        idUserCache.put(user.getId().toString(), user);

        //加入流水
        long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }


    /**
     * 注册
     *
     * @param account
     * @param nickName
     * @param password
     * @return
     */
    private User register(String account, String nickName, String password, String inviteCode, String inviterId) {
        if (userMapper.findByAccount(account) != null) {
            throw new UserException(DUPLICATE_ACCOUNT);
        }

        User user = new User();
        user. register(account, nickName, password, inviteCode, inviterId);
        return save(user) ? user : null;
    }

    private User registerAdmin(String account, String nickName, String password) {
        if (userMapper.findByAccount(account) != null) {
            throw new UserException(DUPLICATE_ACCOUNT);
        }

        User user = new User();
        user.registerAdmin(account, nickName, password);
        return save(user) ? user : null;
    }



    /**
     * 冻结
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserOperatorResponse freeze(Long userId) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.findById(userId);
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));
        if (user.getStatus() == UserStateEnum.FROZEN.getValue()) {
            userOperatorResponse.setSuccess(true);
            return userOperatorResponse;
        }
        //第一次删除缓存
        idUserCache.remove(user.getId().toString());

        if (user.getStatus() == UserStateEnum.FROZEN.getValue()) {
            userOperatorResponse.setSuccess(true);
            return userOperatorResponse;
        }
        user.setStatus(UserStateEnum.FROZEN.getValue());
        boolean updateResult = updateById(user);
        Assert.isTrue(updateResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
        //加入流水
        long result = userOperateStreamService.insertStream(user, UserOperateTypeEnum.FREEZE);
        Assert.notNull(result, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        //第二次删除缓存
        userCacheDelayDeleteService.delayedCacheDelete(idUserCache, user);

        userOperatorResponse.setSuccess(true);
        refreshUserInSession(user.getId().toString());
        return userOperatorResponse;
    }

    /**
     * 解冻
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserOperatorResponse unfreeze(Long userId) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.findById(userId);
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));

        //第一次删除缓存
        idUserCache.remove(user.getId().toString());

        if (user.getStatus() == UserStateEnum.INIT.getValue()) {
            userOperatorResponse.setSuccess(true);
            return userOperatorResponse;
        }
        user.setStatus(UserStateEnum.INIT.getValue());
        //更新数据库
        boolean updateResult = updateById(user);
        Assert.isTrue(updateResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
        //加入流水
        long result = userOperateStreamService.insertStream(user, UserOperateTypeEnum.UNFREEZE);
        Assert.notNull(result, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        //第二次删除缓存
        userCacheDelayDeleteService.delayedCacheDelete(idUserCache, user);

        userOperatorResponse.setSuccess(true);

        refreshUserInSession(user.getId().toString());
        return userOperatorResponse;
    }

    /**
     * 分页查询用户信息
     *
     * @param keyWord
     * @param state
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageResponse<User> pageQueryByState(String keyWord, String state, int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getStatus, state)
                .like(StringUtils.isNotBlank(keyWord),User::getUserAccount, keyWord)
                .orderBy(true, true, User::getCreateTime);

        Page<User> userPage = this.page(page, wrapper);

        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);
    }

    /**
     * 通过手机号和密码查询用户信息
     *
     * @param account
     * @param password
     * @return
     */
    @Override
    public User findByAccountAndPass(String account, String password) {
        return userMapper.findByAccountAndPass(account, DigestUtil.md5Hex(password));
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param userName
     * @return
     */
    @Override
    public User findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }

    @Override
    public UserOperatorResponse adminRegister(String userAccount) {


        String defaultNickName;
        String randomString;
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            //前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_ADMIN_NICK_NAME_PREFIX + randomString + userAccount.substring(7, 11);
        } while (nickNameExist(defaultNickName) || inviteCodeExist(randomString));

        String inviterId = null;

        String password = "admin2025!";
        User user = register(userAccount, defaultNickName, password, randomString, inviterId);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());

        addUserName(defaultNickName);
        addInviteCode(randomString);
        updateInviteRank(inviterId);
        updateUserCache(user.getId().toString(), user);
        //加入流水
        long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    @Override
    public PageResponse<User> userVoList(UserQueryParams registerParams) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(registerParams.getId() != null, User::getId, registerParams.getId())
                .eq(registerParams.getUserAccount() != null, User::getUserAccount, registerParams.getUserAccount())
                .like(registerParams.getUserName() != null, User::getUserName, registerParams.getUserName())
                .orderBy(true, true, User::getCreateTime);
        Page<User> page = new Page<>(registerParams.getCurrentPage(), registerParams.getPageSize());
        Page<User> userPage = this.page(page, wrapper);
        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), registerParams.getPageSize(), registerParams.getCurrentPage());
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param telephone
     * @return
     */
    @Override
    public User findByTelephone(String telephone) {
        return userMapper.findByAccount(telephone);
    }

    /**
     * 通过用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    @Cached(name = ":user:cache:id:", cacheType = CacheType.BOTH, key = "#userId", cacheNullValue = true)
    @CacheRefresh(refresh = 60, timeUnit = TimeUnit.MINUTES)
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    /**
     * 更新用户信息
     *
     * @param userModifyRequest
     * @return
     */
    @CacheInvalidate(name = ":user:cache:id:", key = "#userModifyRequest.userId")
    @Transactional
    @Override
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.findById(userModifyRequest.getUserId());
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));
        Assert.isTrue(user.canModifyInfo(), () -> new UserException(USER_STATUS_CANT_OPERATE));

        if (StringUtils.isNotBlank(userModifyRequest.getUserName()) && nickNameExist(userModifyRequest.getUserName())) {
            throw new UserException(USER_NAME_EXIST);
        }
        BeanUtils.copyProperties(userModifyRequest, user);

        if (StringUtils.isNotBlank(userModifyRequest.getNewPassword())) {
            user.setPasswordHash(DigestUtil.md5Hex(userModifyRequest.getNewPassword()));
        }
        if (updateById(user)) {
            //加入流水
            long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.MODIFY);
            Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
            addUserName(userModifyRequest.getUserName());
            userOperatorResponse.setSuccess(true);
            return userOperatorResponse;
        }
        userOperatorResponse.setSuccess(false);
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getCode());
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getMessage());

        return userOperatorResponse;
    }

    @Override
    public Integer getInviteRank(String userId) {
        Integer rank = inviteRank.revRank(userId);
        if (rank != null) {
            return rank + 1;
        }
        return null;
    }

    @Override
    public PageResponse<User> getUsersByInviterId(String inviterCode, int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getInviteCode, inviterCode)
                .select(User::getUserName, User::getCreateTime)
                .orderBy(true, false, User::getCreateTime);

        Page<User> userPage = this.page(page, wrapper);
        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);
    }

    @Override
    public List<InviteRankInfo> getTopN(Integer topN) {
        Collection<ScoredEntry<String>> rankInfos = inviteRank.entryRangeReversed(0, topN - 1);

        List<InviteRankInfo> inviteRankInfos = new ArrayList<>();

        if (rankInfos != null) {
            for (ScoredEntry<String> rankInfo : rankInfos) {
                InviteRankInfo inviteRankInfo = new InviteRankInfo();
                String userId = rankInfo.getValue();
                if (StringUtils.isNotBlank(userId)) {
                    User user = findById(Long.valueOf(userId));
                    if (user != null) {
                        inviteRankInfo.setNickName(user.getUserName());
                        inviteRankInfo.setInviteCode(user.getInviteCode());
                        inviteRankInfo.setInviteScore(rankInfo.getScore().intValue());
                        inviteRankInfos.add(inviteRankInfo);
                    }
                }
            }
        }

        return inviteRankInfos;
    }


    @Override
    public boolean nickNameExist(String nickName) {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.nickNameBloomFilter != null && this.nickNameBloomFilter.contains(nickName)) {
            return userMapper.findByNickname(nickName) != null;
        }

        return false;
    }

    @Override
    public boolean inviteCodeExist(String inviteCode) {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.inviteCodeBloomFilter != null && this.inviteCodeBloomFilter.contains(inviteCode)) {
            return userMapper.findByInviteCode(inviteCode) != null;
        }

        return false;
    }

    @Override
    public UserQueryResponse<UserInfo> queryUser(UserQueryRequest userQueryRequest) {
        //使用switch表达式精简代码，如果这里编译不过，参考我的文档调整IDEA的JDK版本
        //文档地址：https://thoughts.aliyun.com/workspaces/6655879cf459b7001ba42f1b/docs/6673f26c5e11940001c810fb#667971268a5c151234adcf92
        User user = switch (userQueryRequest.getUserQueryCondition()) {
            case UserIdQueryCondition userIdQueryCondition:
                yield this.findById(userIdQueryCondition.getUserId());
            case UserPhoneQueryCondition userPhoneQueryCondition:
                yield this.findByTelephone(userPhoneQueryCondition.getTelephone());
            case UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition:
                yield this.findByAccountAndPass(userPhoneAndPasswordQueryCondition.getUserAccount(),userPhoneAndPasswordQueryCondition.getPassword());
            default:
                throw new UnsupportedOperationException(userQueryRequest.getUserQueryCondition() + "'' is not supported");
        };

        UserQueryResponse<UserInfo> response = new UserQueryResponse();
        response.setSuccess(true);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        response.setData(userInfo);
        return response;
    }


    public void checkSelfOrAdmin(Long targetUserId) {
        // 获取当前登录用户ID
        long loginUserId = StpUtil.getLoginIdAsLong();

        // 如果是本人或者管理员，则通过
        boolean isSelfOrAdmin = loginUserId == targetUserId || StpUtil.hasRole("admin");
        if (!isSelfOrAdmin) {
            throw new BizException(AUTH_ERROR_CODE);
        }
    }

    private boolean addUserName(String nickName) {
        return this.nickNameBloomFilter != null && this.nickNameBloomFilter.add(nickName);
    }

    private boolean addInviteCode(String inviteCode) {
        return this.inviteCodeBloomFilter != null && this.inviteCodeBloomFilter.add(inviteCode);
    }

    private void updateInviteRank(String inviterId) {
        if (inviterId == null) {
            return;
        }
        RLock rLock = redissonClient.getLock(inviterId);
        rLock.lock();
        try {
            Double score = inviteRank.getScore(inviterId);
            if (score == null) {
                score = 0.0;
            }
            inviteRank.add(score + 100.0, inviterId);
        } finally {
            rLock.unlock();
        }
    }

    private void updateUserCache(String userId, User user) {
        idUserCache.put(userId, user);
    }

    private void refreshUserInSession(String userId) {
        User user = this.getById(userId);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.nickNameBloomFilter = redissonClient.getBloomFilter("userName");
        if (nickNameBloomFilter != null && !nickNameBloomFilter.isExists()) {
            this.nickNameBloomFilter.tryInit(100000L, 0.01);
        }

        this.inviteCodeBloomFilter = redissonClient.getBloomFilter("inviteCode");
        if (inviteCodeBloomFilter != null && !inviteCodeBloomFilter.isExists()) {
            this.inviteCodeBloomFilter.tryInit(100000L, 0.01);
        }

        this.inviteRank = redissonClient.getScoredSortedSet("inviteRank");
    }
}
