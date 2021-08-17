package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.exception.PasswordErrorException;
import cn.wghtstudio.insurance.exception.SignTokenException;
import cn.wghtstudio.insurance.exception.UserNotFoundException;
import cn.wghtstudio.insurance.service.entity.LoginResponseBody;
import cn.wghtstudio.insurance.service.LoginService;
import cn.wghtstudio.insurance.service.entity.LoginResponseBodyBuilder;
import cn.wghtstudio.insurance.util.PasswordMD5;
import cn.wghtstudio.insurance.util.Token;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginImpl implements LoginService {
    @Resource
    UserRepository userRepository;

    @Resource
    Token token;

    @Override
    public LoginResponseBody UserLoginService(String username, String password)
            throws PasswordErrorException, UserNotFoundException, SignTokenException {
        User user = userRepository.getUserByUsername(username);
        // 用户没有找到
        if (user == null) {
            throw new UserNotFoundException();
        }

        // 密码错误
        if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(password))) {
            throw new PasswordErrorException();
        }

        String token;
        try {
            token = this.token.sign(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignTokenException();
        }

        return LoginResponseBodyBuilder.getInstance().
                withID(user.getId()).
                withToken(token).
                withUsername(user.getUsername()).
                withRole(user.getRole()).
                build();
    }
}
