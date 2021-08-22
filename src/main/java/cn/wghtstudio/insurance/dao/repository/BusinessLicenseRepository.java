package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.BusinessLicense;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessLicenseRepository {
    Integer createBusinessLicense(BusinessLicense businessLicense);
}
