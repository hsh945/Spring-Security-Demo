package vip.erichong.chasingDreams.exception;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.erichong.chasingDreams.common.Result;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eric
 */
@RestControllerAdvice
public class GlobalException {

    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return Result.fail(message);
    }

    @ExceptionHandler(CaptchaException.class)
    public Result<?> captchaExceptionHandle(CaptchaException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> illegalArgumentExceptionHandle(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public Result<?> jwtExceptionHandle(JwtException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result<?> unauthorizedExceptionHandle(UnauthorizedException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandle(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
