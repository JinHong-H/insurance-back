package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Policy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyRepository {
    void createPolicy(Policy policy);

    void updatePolicy(Policy policy);
    
    List<Policy> selectPolicyByorderid(Integer orderID);
}
