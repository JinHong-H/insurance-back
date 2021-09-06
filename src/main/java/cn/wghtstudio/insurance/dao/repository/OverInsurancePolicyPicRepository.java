package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.OverInsurancePolicyPic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OverInsurancePolicyPicRepository {
    void createOverInsurancePolicyPic(OverInsurancePolicyPic overInsurancePolicyPic);

    List<OverInsurancePolicyPic> getOverInsurancePolicyPicListByOrderId(OverInsurancePolicyPic overInsurancePolicyPic);

    void deleteOverInsurancePolicyPic(int id);
}
