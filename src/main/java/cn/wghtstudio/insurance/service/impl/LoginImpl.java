package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.exception.PasswordErrorException;
import cn.wghtstudio.insurance.exception.UserNotFoundException;
import cn.wghtstudio.insurance.service.entity.LoginResponseBody;
import cn.wghtstudio.insurance.service.LoginService;
import cn.wghtstudio.insurance.service.entity.LoginResponseBodyBuilder;
import cn.wghtstudio.insurance.util.PasswordMD5;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginImpl implements LoginService {
    @Resource
    UserRepository userRepository;

    @Override
    public LoginResponseBody UserLoginService(String username, String password)
            throws PasswordErrorException, UserNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(password))) {
            throw new PasswordErrorException();
        }

        return LoginResponseBodyBuilder.getInstance().build();
    }
}
