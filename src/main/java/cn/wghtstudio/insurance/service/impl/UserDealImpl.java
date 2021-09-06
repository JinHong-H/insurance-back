package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.exception.UserExistedException;
import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.GetUserListItem;
import cn.wghtstudio.insurance.service.entity.GetUserListResponseBody;
import cn.wghtstudio.insurance.util.PasswordMD5;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserDealImpl implements UserDealService {
    @Resource
    UserRepository userRepository;

    @Override
    public GetUserListResponseBody getUserListService(Map<String, Object> params) {
        GetUserListResponseBody.GetUserListResponseBodyBuilder builder = GetUserListResponseBody.builder();

        int total = userRepository.getUserCount(params);
        builder.total(total);

        List<User> users = userRepository.queryUser(params);
        List<GetUserListItem> items = users.stream().
                map((item) -> GetUserListItem.builder().
                        id(item.getId()).
                        username(item.getUsername()).
                        nickname(item.getNickname()).
                        role(item.getRole().getValue()).
                        build()
                ).collect(Collectors.toList());

        builder.items(items);

        return builder.build();
    }

    @Override
    public void addUserService(String username, String nickname, String password, int roleId) throws UserExistedException {
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            throw new UserExistedException();
        }

        User.UserBuilder builder = User.builder();
        User createUser = builder.username(username).nickname(nickname).password(PasswordMD5.getPasswordMD5(password)).roleID(roleId).build();
        userRepository.addUser(createUser);
    }

    @Override
    public void updateUserService(int id, String nickname, String password, int roleId) {
        User.UserBuilder builder = User.builder();
        User updateUser = builder.id(id).nickname(nickname).password(PasswordMD5.getPasswordMD5(password)).roleID(roleId).build();
        userRepository.updateUser(updateUser);
    }

    @Override
    public void deleteUserService(int id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public void updateOwnPasswordService(int id, String newPassword) {
        User.UserBuilder builder = User.builder();
        User updateUser = builder.id(id).password(PasswordMD5.getPasswordMD5(newPassword)).build();

        userRepository.updateUser(updateUser);
    }
}
