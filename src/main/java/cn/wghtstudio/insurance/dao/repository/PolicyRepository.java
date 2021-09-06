package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Policy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PolicyRepository {
    void createPolicy(List<Policy> policy);

    void updatePolicy(Policy policy);

    List<Policy> getPolicyList(Map<String, Object> params);

    List<Policy> selectPolicyByOrderId(Integer orderID);

    Integer getPolicyCount();

    void deletePolicy(int id);
}
