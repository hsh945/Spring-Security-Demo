package vip.erichong.chasingDreams.security;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vip.erichong.chasingDreams.common.AccountUser;
import vip.erichong.chasingDreams.common.Result;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.user.dto.LoginResultDto;
import vip.erichong.chasingDreams.utils.JwtUtils;
import vip.erichong.chasingDreams.utils.RedisUtil;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author eric
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final RedisUtil redisUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        AccountUser accountUser = (AccountUser)authentication.getPrincipal();

        String username = accountUser.getUsername();
        String accessToken = jwtUtils.generateToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        LoginResultDto dto = LoginResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        redisUtil.hset(Constant.LOGIN_CACHE, username, accountUser);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSONObject.toJSONString(Result.success(dto)));
        }
    }
}
