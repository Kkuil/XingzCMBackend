package top.kkuily.xingbackend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 小K
 * @description 返回结果统一类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private int status;
    private String msg;
    private Object data;
    private ErrorType type;

    public static Result success() {
        return new Result(200, "请求成功", true, ErrorType.SILENT);
    }

    public static Result success(String msg) {
        return new Result(200, msg, true, ErrorType.SILENT);
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data, ErrorType.SILENT);
    }

    public static Result fail() {
        return new Result(400, "请求失败", false, ErrorType.NOTIFICATION);
    }

    public static Result fail(int status, String msg, ErrorType type) {
        return new Result(status, msg, false, type);
    }

    public static Result fail(int status, String msg,Object data, ErrorType type) {
        return new Result(status, msg, data, type);
    }
}
