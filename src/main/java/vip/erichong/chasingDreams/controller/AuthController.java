package vip.erichong.chasingDreams.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.erichong.chasingDreams.common.Result;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.user.dto.LoginResultDto;
import vip.erichong.chasingDreams.user.service.LoginService;
import vip.erichong.chasingDreams.utils.RedisUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author eric
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final RedisUtil redisUtil;
    private final LoginService loginService;

    @GetMapping("/logout")
    public Result<String> logout() {
        return loginService.logout();
    }

    @GetMapping("/refreshToken")
    public Result<LoginResultDto> refreshToken() {
        return loginService.refreshToken();
    }


    @GetMapping("/captcha")
    public Result<Object> captcha() throws IOException {
        String key = UUID.randomUUID().toString();

        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String text = captcha.text();
        String base64 = captcha.toBase64();

        redisUtil.hset(Constant.CAPTCHA, key, text, 1200);

        Map<String, Object> result = new HashMap<>();
        result.put("token", key);
        result.put("base64Img", base64);

        return Result.success(result);
    }
}
