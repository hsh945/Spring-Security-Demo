package vip.erichong.chasingDreams.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.erichong.chasingDreams.role.entity.SystemUserRole;
import vip.erichong.chasingDreams.role.mapper.SystemUserRoleMapper;
import vip.erichong.chasingDreams.role.service.SystemUserRoleService;

@Service
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements SystemUserRoleService {
}
