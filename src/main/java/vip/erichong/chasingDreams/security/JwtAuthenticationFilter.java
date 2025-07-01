package vip.erichong.chasingDreams.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vip.erichong.chasingDreams.common.AccountUser;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.utils.JwtUtils;
import vip.erichong.chasingDreams.utils.RedisUtil;

import java.io.IOException;


/**
 * @author eric
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String jwt = request.getHeader(jwtUtils.getHeader());
            if (StringUtils.isBlank(jwt)) {
                chain.doFilter(request, response);
                return;
            }

            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim == null) {
                throw new JwtException("token 异常");
            }
            if (jwtUtils.isTokenExpire(claim)) {
                throw new JwtException("token 已过期");
            }

            String username = claim.getSubject();

            Object userCache = redisUtil.hget(Constant.LOGIN_CACHE, username);

            if (userCache == null) {
                throw new JwtException("用户未登录");
            }

            AccountUser accountUser = (AccountUser) userCache;
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountUser, null, accountUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        } catch (JwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        chain.doFilter(request, response);
    }
}
