package cn.wghtstudio.insurance.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum ResultMsg {
    SUCCESS_MSG("成功"), ERROR_MSG("失败");

    private String msg;
}

@Data
public class Result<V> {
    private int code;
    private V data;
    private String msg;

    public static <T> Result<T> success(T data) {
        final Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(ResultMsg.SUCCESS_MSG.getMsg());
        result.setData(data);

        return result;
    }

    public static Result<Void> success() {
        final Result<Void> result = new Result<>();
        result.setCode(0);
        result.setMsg(ResultMsg.SUCCESS_MSG.getMsg());
        result.setData(null);

        return result;
    }

    public static <T> Result<T> error(T data) {
        final Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(ResultMsg.ERROR_MSG.getMsg());
        result.setData(data);

        return result;
    }

    public static Result<Void> error() {
        final Result<Void> result = new Result<>();
        result.setCode(0);
        result.setMsg(ResultMsg.ERROR_MSG.getMsg());
        result.setData(null);

        return result;
    }
}
