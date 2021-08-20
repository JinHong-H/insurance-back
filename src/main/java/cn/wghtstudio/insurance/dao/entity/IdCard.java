package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

@Data
public class IdCard {
    private int id;
    private String url;
    private String name;
    private String number;
    private String address;
    private int orderId;
}
