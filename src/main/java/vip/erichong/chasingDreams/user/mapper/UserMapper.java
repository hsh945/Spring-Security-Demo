package vip.erichong.chasingDreams.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.erichong.chasingDreams.user.entity.User;

import java.util.List;

/**
 * @author eric
 */
public interface UserMapper extends BaseMapper<User> {

    List<Long> getNavigationMenuIds(Long userId);

    List<User> listByMenuId(Long menuId);
}
