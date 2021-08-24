package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.GetUserListItem;
import cn.wghtstudio.insurance.service.entity.GetUserListResponseBody;
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
                        role(item.getRole().getValue()).
                        build()
                ).collect(Collectors.toList());

        builder.items(items);

        return builder.build();
    }

    @Override
    public void addUserService(String username, String password, int roleId) {
        userRepository.addUser(username, password, roleId);
    }

    @Override
    public void deleteUserService(int id) {
        userRepository.deleteUserById(id);
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
