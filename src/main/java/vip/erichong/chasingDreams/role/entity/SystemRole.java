package vip.erichong.chasingDreams.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
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
public class SystemRole extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    private String code;

    /**
     * 备注
     */
    private String remark;

    private Integer status;

    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();
}
