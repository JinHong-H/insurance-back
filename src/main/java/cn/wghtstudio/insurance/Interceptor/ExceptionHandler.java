package cn.wghtstudio.insurance.Interceptor;

import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Result<String> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        return Result.error(ResultEnum.ARGUMENT_ERROR, e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> argumentExceptionHandler(MethodArgumentNotValidException e) {
        return Result.error(ResultEnum.ARGUMENT_ERROR, e.getMessage());
    }
}
