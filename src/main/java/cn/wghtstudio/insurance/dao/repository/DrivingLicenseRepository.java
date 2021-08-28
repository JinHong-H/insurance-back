package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.DrivingLicense;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DrivingLicenseRepository {
    void createDrivingLicense(DrivingLicense drivingLicense);

    void updateDrivingLicense(DrivingLicense drivingLicense);

    List<DrivingLicense> getDrivingLicenseByPolicyInfo(DrivingLicense drivingLicense);
}
