package vip.erichong.chasingDreams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.erichong.chasingDreams.common.Result;
import vip.erichong.chasingDreams.user.entity.User;

/**
 * @author eric
 */
@RestController
@RequestMapping(value = "/system/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/list")
    // 应用了注解的接口会被 Security 控制拦截，并判断是否有权限才可调通 API
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<Page<User>> list(String username) {
        // todo 你的业务逻辑
        return Result.success(null);
    }
}
