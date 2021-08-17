package cn.wghtstudio.insurance.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(0, "请求成功"),
    DEFAULT_ERROR(10001, "请求失败"),
    ARGUMENT_ERROR(10002, "参数错误"),
    PASSWORD_ERROR(20001, "密码错误"),
    USER_NOT_FOUND(20002, "用户未找到");

    private int code;
    private String msg;
}
