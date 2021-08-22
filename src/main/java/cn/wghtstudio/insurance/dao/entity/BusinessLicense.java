package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessLicense {
    private int id;
    private String url;
    private String name;
    private String address;
    private String number;
    private int orderId;
}
