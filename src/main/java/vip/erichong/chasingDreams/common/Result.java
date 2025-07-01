package vip.erichong.chasingDreams.common;

import lombok.Data;

/**
 * @author eric
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功返回结果
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    // 错误返回结果
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
