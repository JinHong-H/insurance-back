package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.service.entity.GetUserListResponseBody;

import java.util.Map;

public interface UserDealService {
    GetUserListResponseBody getUserListService(Map<String, Object> params);

    void addUserService(String username, String password, int roleId);

    void deleteUserService(int id);

    void updateUserService(int id, String username, String password, int roleId);

    void updateOwnPasswordService(int id, String newpassword);
}
