package vip.erichong.chasingDreams.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.erichong.chasingDreams.role.entity.SystemRole;
import vip.erichong.chasingDreams.role.mapper.SystemRoleMapper;
import vip.erichong.chasingDreams.role.service.SystemRoleService;

import java.util.List;

@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService {
    @Override
    public List<SystemRole> listRolesByUserId(Long userId) {
        return this.list(new QueryWrapper<SystemRole>()
                .inSql("id", "select role_id from system_user_role where user_id = " + userId));
    }
}
