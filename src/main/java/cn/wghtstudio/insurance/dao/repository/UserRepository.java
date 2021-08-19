package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRepository {
    User getUserByUsername(String username);

    User getUserByID(int id);
    void addUser(String username,String password,int roleId);
    void deleteUserById(int id);
    void updateUserById(int id,String username,String password,int roleId);
    User[] queryUser(String question,int pageSize,int offset);
}
