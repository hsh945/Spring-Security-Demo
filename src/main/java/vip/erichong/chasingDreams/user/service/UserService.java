package vip.erichong.chasingDreams.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.erichong.chasingDreams.user.entity.User;

/**
 * @author eric
 */
public interface UserService extends IService<User> {
    User getByUsername(String username);

    String getUserAuthorityInfo(Long userId);

    /**
     * 清除redis缓存，权限变更时
     */
    void clearUserAuthorityInfo(String username);
    void clearUserAuthorityInfoByRoleId(Long roleId);
    void clearUserAuthorityInfoByMenuId(Long menuId);
}
