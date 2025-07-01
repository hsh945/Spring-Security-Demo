package vip.erichong.chasingDreams.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author eric
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
