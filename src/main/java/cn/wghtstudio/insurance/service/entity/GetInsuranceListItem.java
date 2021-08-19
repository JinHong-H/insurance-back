package cn.wghtstudio.insurance.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetInsuranceListItem {
    private int id;
    private String owner;
    private String licensePlate;
    private String startTime;
    private String carType;
    private String payType;
}
