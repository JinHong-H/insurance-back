package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.OverInsurancePolicy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OverInsurancePolicyRepository {
    void createOverInsurancePolicy(OverInsurancePolicy overInsurancePolicy);

    OverInsurancePolicy getOverInsurancePolicyByOrderId(OverInsurancePolicy overInsurancePolicy);
}
