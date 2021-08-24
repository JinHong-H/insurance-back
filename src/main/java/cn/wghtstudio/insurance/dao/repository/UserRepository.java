package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserRepository {
    User getUserByUsername(String username);

    User getUserByID(int id);

    List<User> queryUser(Map<String, Object> params);

    int getUserCount(Map<String, Object> params);

    void addUser(String username, String password, int roleId);

    void deleteUserById(int id);

    void updateUserById(int id, String username, String password, int roleId);

    void updateUserPassword(int id, String newpassword);
}
