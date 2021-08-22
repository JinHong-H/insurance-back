package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdCard {
    private int id;
    private String url;
    private String name;
    private String number;
    private String address;
    private int orderId;
}
