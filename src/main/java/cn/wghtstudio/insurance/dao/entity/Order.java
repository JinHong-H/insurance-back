package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class Order {
    private int id;
    private Date createAt;
    private int createBy;
    private Date startTime;
    private int fileType;
    private int paymentId;
    private int carTypeId;

    private Payment payment;
    private CarType carType;

    private User user;

    private IdCard idCard;
    private BusinessLicense businessLicense;
    private DrivingLicense drivingLicense;
    private Certificate certificate;
    private List<OtherFile> otherFileList;
    private Policy policy;
    private OverInsurancePolicy overInsurancePolicy;
    private OverInsurancePolicyPic overInsurancePolicyPic;

    @Tolerate
    public Order() {
    }
}
