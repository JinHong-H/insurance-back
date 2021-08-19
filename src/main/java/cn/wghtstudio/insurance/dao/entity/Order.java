package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
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
}
