package vip.erichong.chasingDreams.security;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import vip.erichong.chasingDreams.common.Result;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author eric
 */
@Component
public class JwtAccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Result<String> result = Result.fail(HttpStatus.FORBIDDEN.value(), "您的权限不足");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSONObject.toJSONString(result));
        }
    }
}
