package vip.erichong.chasingDreams.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.erichong.chasingDreams.role.entity.SystemRole;

import java.util.List;

/**
 * @author eric
 */
public interface SystemRoleService extends IService<SystemRole> {
    List<SystemRole> listRolesByUserId(Long userId);
}
