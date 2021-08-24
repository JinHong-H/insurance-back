package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.QueryUserResponseBody;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserDealImpl implements UserDealService {
    @Resource
    UserRepository userRepository;

    @Override
    public void addUserService(String username, String password, int roleId) {
        userRepository.addUser(username, password, roleId);
    }

    @Override
    public void deleteUserService(int id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public QueryUserResponseBody[] queryUserService(String question, int pageSize, int offset) {
        User[] users = userRepository.queryUser(question, pageSize, offset);
        int length = users.length;
        if (length == 0) {
            return null;
        }
        QueryUserResponseBody[] res = new QueryUserResponseBody[length];

        for (int i = 0; i < length; i++) {
            res[i] = QueryUserResponseBody.builder().
                    id(users[i].getId()).
                    username(users[i].getUsername()).
                    roleID(users[i].getRoleID()).
                    build();
        }

        return res;
    }

    @Override
    public void updateUserService(int id, String username, String password, int roleId) {
        userRepository.updateUserById(id, username, password, roleId);
    }


    @Override
    public void updateOwnPasswordService(int id, String newpassword) {
        userRepository.updateUserPassword(id, newpassword);
    }
}
