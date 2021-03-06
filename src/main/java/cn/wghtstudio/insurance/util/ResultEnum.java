package cn.wghtstudio.insurance.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(0, "请求成功"),
    DEFAULT_ERROR(10001, "请求失败"),
    ARGUMENT_ERROR(10002, "参数错误"),
    SIGN_TOKEN_ERROR(10003, "生成签名失败"),
    PASSWORD_ERROR(20001, "密码错误"),
    USER_NOT_FOUND(20002, "用户未找到"),
    AUTH_ERROR(20003, "权限不满足"),
    PARSE_TOKEN_ERROR(20004, "Token 解析失败"),
    JSON_PARSE_ERROR(20005, "json解析失败"),
    TOKEN_GET_ERROR(20006, "OCR获取签名失败"),
    OCR_ERROR(20007, "OCR 失败，请调整图片"),
    ALIYUN_AUTH_ERROR(20008, "阿里云授权失败，请重试"),
    USER_EXISTED_ERROR(20009, "用户已存在，请检查用户名"),
    FILE_TYPE_ERROR(20010, "请检查上传的文件格式，仅支持zip与pdf");

    private int code;
    private String msg;
}
