package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

@Data
public class Policy {
    private int id;
    private String url;
    private String number;
    private int orderId;
}
