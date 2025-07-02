package vip.erichong.chasingDreams.security;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import vip.erichong.chasingDreams.common.Result;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author eric
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 返回自定义格式的失败响应
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Result<String> result = Result.fail(HttpStatus.UNAUTHORIZED.value(), "认证失败");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSONObject.toJSONString(result));
        }
    }
}
