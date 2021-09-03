package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetInsuranceListItem {
    private int id;
    private String owner;
    private String licensePlate;
    private String startTime;
    private String carType;
    private String payType;
    private String policy;
    private String username;
}
