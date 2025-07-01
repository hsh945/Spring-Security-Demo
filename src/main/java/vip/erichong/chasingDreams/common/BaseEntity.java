package vip.erichong.chasingDreams.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author eric
 */
@Data
public class BaseEntity implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private Integer deleted;
}
