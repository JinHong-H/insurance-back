package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRepository {
    User getUserByUsername(String username);

    User getUserByID(int id);
}
