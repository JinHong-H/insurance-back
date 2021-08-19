package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

@Data
public class DrivingLicense {
    private int id;
    private String url;
    private String owner;
    private String plateNumber;
    private String engine;
    private String frame;
    private String type;
    private int orderId;
}
