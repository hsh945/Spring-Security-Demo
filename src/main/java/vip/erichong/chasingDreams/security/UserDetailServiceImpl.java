package vip.erichong.chasingDreams.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vip.erichong.chasingDreams.common.AccountUser;
import vip.erichong.chasingDreams.user.entity.User;
import vip.erichong.chasingDreams.user.service.UserService;

import java.util.List;

/**
 * @author eric
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户名或密码不正确");
        }
        List<GrantedAuthority> userAuthority = getUserAuthority(user.getId());
        return new AccountUser(user, userAuthority);
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String userAuthorityInfo = userService.getUserAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userAuthorityInfo);
    }
}
