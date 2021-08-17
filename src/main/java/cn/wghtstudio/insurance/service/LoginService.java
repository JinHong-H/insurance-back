package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.PasswordErrorException;
import cn.wghtstudio.insurance.exception.UserNotFoundException;
import cn.wghtstudio.insurance.service.entity.LoginResponseBody;

public interface LoginService {
    LoginResponseBody UserLoginService(String username, String password)
            throws PasswordErrorException, UserNotFoundException;
}
