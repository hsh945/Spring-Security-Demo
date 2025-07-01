package vip.erichong.chasingDreams.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.menu.entity.SystemMenu;
import vip.erichong.chasingDreams.menu.service.SystemMenuService;
import vip.erichong.chasingDreams.role.entity.SystemRole;
import vip.erichong.chasingDreams.role.service.SystemRoleService;
import vip.erichong.chasingDreams.user.entity.User;
import vip.erichong.chasingDreams.user.mapper.UserMapper;
import vip.erichong.chasingDreams.user.service.UserService;
import vip.erichong.chasingDreams.utils.RedisUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eric
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final RedisUtil redisUtil;
    private final SystemRoleService systemRoleService;
    private final SystemMenuService systemMenuService;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getDeleted, Constant.NO_DELETE);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        User user = userMapper.selectById(userId);

        String authority = "";

        if (redisUtil.hasKey(Constant.USER_AUTHORITY_KEY + user.getUsername())) {
            authority = (String) redisUtil.get(Constant.USER_AUTHORITY_KEY  + user.getUsername());
        } else {
            // 获取角色编码
            LambdaQueryWrapper<SystemRole> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.inSql(SystemRole::getId, "select role_id from system_user_role where deleted = 0 and user_id = " + userId);
            queryWrapper.eq(SystemRole::getDeleted, Constant.NO_DELETE);
            List<SystemRole> roles = systemRoleService.list(queryWrapper);


            if (!roles.isEmpty()) {
                // security 框架约定好的, 以 ROLE_ 开头的是角色
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            // 获取菜单操作编码
            List<Long> menuIds = userMapper.getNavigationMenuIds(userId);
            if (!menuIds.isEmpty()) {
                List<SystemMenu> menus = systemMenuService.listByIds(menuIds);
                String menuPerms = menus.stream().map(SystemMenu::getPermissions).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
            }

            redisUtil.set(Constant.USER_AUTHORITY_KEY  + user.getUsername(), authority, 60 * 60);
        }

        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del(Constant.USER_AUTHORITY_KEY  + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.inSql(User::getId, "select user_id from system_user_role where role_id = " + roleId);
        List<User> users = this.list(queryWrapper);

        users.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<User> sysUsers = userMapper.listByMenuId(menuId);

        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}
