package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.exception.PasswordErrorException;
import cn.wghtstudio.insurance.exception.SignTokenException;
import cn.wghtstudio.insurance.exception.UserNotFoundException;
import cn.wghtstudio.insurance.service.LoginService;
import cn.wghtstudio.insurance.service.entity.LoginResponseBody;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private LoginService loginService;

    @Getter
    @Setter
    static class LoginRequestBody {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @PostMapping("/login")
    public Result<LoginResponseBody> Login(@Valid @RequestBody LoginRequestBody req) {
        try {
            LoginResponseBody res = loginService.UserLoginService(req.username, req.password);
            return Result.success(res);
        } catch (PasswordErrorException e) {
            logger.warn("PasswordErrorException", e);
            return Result.error(ResultEnum.PASSWORD_ERROR);
        } catch (UserNotFoundException e) {
            logger.warn("UserNotFoundException", e);
            return Result.error(ResultEnum.USER_NOT_FOUND);
        } catch (SignTokenException e) {
            logger.warn("SignTokenException", e);
            return Result.error(ResultEnum.SIGN_TOKEN_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
