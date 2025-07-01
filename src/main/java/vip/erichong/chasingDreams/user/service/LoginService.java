package vip.erichong.chasingDreams.user.service;


import vip.erichong.chasingDreams.common.Result;
import vip.erichong.chasingDreams.user.dto.LoginResultDto;

/**
 * @author eric
 */
public interface LoginService {
    Result<String> logout();
    Result<LoginResultDto> refreshToken();
}
