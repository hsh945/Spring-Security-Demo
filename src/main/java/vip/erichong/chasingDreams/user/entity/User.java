package vip.erichong.chasingDreams.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.erichong.chasingDreams.common.BaseEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author eric
 */
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@Data
public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String phone;
    private String nickName;
    private String firstName;
    private String lastName;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    private Integer gender;
    private Date birthDay;
    private String remark;

    @TableField(exist = false)
    private List<?> systemRoles = new ArrayList<>();
}
