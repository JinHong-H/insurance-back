package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Policy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PolicyRepository {
    void createPolicy(Policy policy);
    
    void updatePolicy(Policy policy);
}
