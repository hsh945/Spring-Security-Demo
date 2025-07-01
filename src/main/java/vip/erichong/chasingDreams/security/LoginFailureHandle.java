package vip.erichong.chasingDreams.security;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import vip.erichong.chasingDreams.common.Result;

import java.io.IOException;

/**
 * @author eric
 */
@Component
public class LoginFailureHandle implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Result<String> result = Result.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}
