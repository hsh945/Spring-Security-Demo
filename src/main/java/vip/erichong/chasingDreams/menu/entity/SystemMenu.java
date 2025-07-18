package vip.erichong.chasingDreams.menu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.erichong.chasingDreams.common.BaseEntity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eric
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMenu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @NotBlank(message = "菜单授权码不能为空")
    private String permissions;

    /**
     * 组件（前端用）
     */
    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;

    private Integer status;

    @TableField(exist = false)
    private List<SystemMenu> children = new ArrayList<>();
}
