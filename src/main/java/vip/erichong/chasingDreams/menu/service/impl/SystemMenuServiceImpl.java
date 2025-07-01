package vip.erichong.chasingDreams.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vip.erichong.chasingDreams.common.AccountUser;
import vip.erichong.chasingDreams.constants.Constant;
import vip.erichong.chasingDreams.menu.dto.SystemMenuDto;
import vip.erichong.chasingDreams.menu.entity.SystemMenu;
import vip.erichong.chasingDreams.menu.mapper.SystemMenuMapper;
import vip.erichong.chasingDreams.menu.service.SystemMenuService;
import vip.erichong.chasingDreams.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author eric
 */
@Service
@RequiredArgsConstructor
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService {
    private final UserMapper sysUserMapper;

    @Override
    public List<SystemMenuDto> getCurrentUserNavigation() {
        AccountUser currentUser = (AccountUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Long> menuIds = sysUserMapper.getNavigationMenuIds(currentUser.getUser().getId());
        LambdaQueryWrapper<SystemMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SystemMenu::getId, menuIds);
        queryWrapper.eq(SystemMenu::getDeleted, Constant.NO_DELETE);
        List<SystemMenu> menus = this.list(queryWrapper);

        // 转树状结构
        List<SystemMenu> menuTree = buildTreeMenu(menus);

        // 实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<SystemMenu> listMenuTree() {
        // 获取所有菜单信息
        List<SystemMenu> sysMenus = this.list(new QueryWrapper<SystemMenu>().orderByAsc("orderNum"));

        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    private List<SystemMenuDto> convert(List<SystemMenu> menuTree) {
        List<SystemMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SystemMenuDto dto = new SystemMenuDto();

            dto.setId(m.getId());
            dto.setName(m.getPermissions());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            dto.setIcon(m.getIcon());
            dto.setType(m.getType());

            if (!m.getChildren().isEmpty()) {
                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<SystemMenu> buildTreeMenu(List<SystemMenu> menus) {

        List<SystemMenu> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SystemMenu menu : menus) {

            for (SystemMenu e : menus) {
                if (Objects.equals(menu.getId(), e.getParentId())) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }

        return finalMenus;
    }
}
