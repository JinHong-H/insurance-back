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

    void addUser(User user);

    void updateUser(User user);

    void deleteUserById(int id);
}
