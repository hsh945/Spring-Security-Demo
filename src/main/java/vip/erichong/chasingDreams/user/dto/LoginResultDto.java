package vip.erichong.chasingDreams.user.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric
 */
@Data
@Builder
public class LoginResultDto implements Serializable {
    private String accessToken;
    private String refreshToken;
}
