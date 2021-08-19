package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

@Data
public class BusinessLicense {
    private int id;
    private String url;
    private String name;
    private String address;
    private String number;
    private int orderId;
}
