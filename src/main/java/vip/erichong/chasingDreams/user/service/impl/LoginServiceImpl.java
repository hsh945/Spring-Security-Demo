package vip.erichong.chasingDreams.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vip.erichong.chasingDreams.common.AccountUser;
import vip.erichong.chasingDreams.common.Result;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.user.dto.LoginResultDto;
import vip.erichong.chasingDreams.user.entity.User;
import vip.erichong.chasingDreams.user.service.LoginService;
import vip.erichong.chasingDreams.utils.JwtUtils;
import vip.erichong.chasingDreams.utils.RedisUtil;

/**
 * @author eric
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    final JwtUtils jwtUtils;
    final RedisUtil redisUtil;

    @Override
    public Result<String> logout() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        AccountUser accountUser = (AccountUser)authenticationToken.getPrincipal();
        redisUtil.hdel(Constant.LOGIN_CACHE, accountUser.getUsername());
        redisUtil.del(Constant.USER_AUTHORITY_KEY + accountUser.getUsername());
        return Result.success("退出成功");
    }

    @Override
    public Result<LoginResultDto> refreshToken() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        AccountUser accountUser = (AccountUser)authenticationToken.getPrincipal();
        String username = accountUser.getUsername();
        String accessToken = jwtUtils.generateToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);
        LoginResultDto build = LoginResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return Result.success(build);
    }
}
