package vip.erichong.chasingDreams.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.exception.CaptchaException;
import vip.erichong.chasingDreams.utils.RedisUtil;

import java.io.IOException;

/**
 * @author eric
 */
@Component
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {
    private final RedisUtil redisUtil;
    private final LoginFailureHandle loginFailureHandle;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String uri = httpServletRequest.getRequestURI();
        if ("/login".equals(uri) && "POST".equals(httpServletRequest.getMethod())) {
            try {
                // 校验验证码
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 如果不正确跳转认证失败处理器
                loginFailureHandle.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest) {
        String captcha = httpServletRequest.getParameter("captcha");
        String key = httpServletRequest.getParameter("token");

        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if (!captcha.equals(redisUtil.hget(Constant.CAPTCHA, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 去除redis中的验证码
        redisUtil.hdel(Constant.CAPTCHA, key);
    }
}
