package vip.erichong.chasingDreams.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.erichong.chasingDreams.menu.dto.SystemMenuDto;
import vip.erichong.chasingDreams.menu.entity.SystemMenu;

import java.util.List;

/**
 * @author eric
 */
public interface SystemMenuService extends IService<SystemMenu> {
    List<SystemMenuDto> getCurrentUserNavigation();

    List<SystemMenu> listMenuTree();
}
