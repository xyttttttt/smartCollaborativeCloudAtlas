package com.xyt.cloudAtlas.business.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.xyt.init.api.user.constant.UserPermission;
import com.xyt.init.api.user.constant.UserRole;
import com.xyt.init.api.user.constant.UserStateEnum;
import com.xyt.init.api.user.response.data.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口
 *
 * @author Hollis
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get((String) loginId);

        if (userInfo.getUserRole() == UserRole.ADMIN ) {
            return List.of(UserPermission.BASIC.name(), UserPermission.AUTH.name());
        }

        if (userInfo.getState() == UserStateEnum.INIT.getValue()) {
            return List.of(UserPermission.BASIC.name());
        }

        if (userInfo.getState()== UserStateEnum.FROZEN.getValue()) {
            return List.of(UserPermission.FROZEN.name());
        }

        return List.of(UserPermission.NONE.name());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get((String) loginId);
        if (userInfo.getUserRole() == UserRole.ADMIN) {
            return List.of(UserRole.ADMIN.name());
        }
        return List.of(UserRole.CUSTOMER.name());
    }


}
