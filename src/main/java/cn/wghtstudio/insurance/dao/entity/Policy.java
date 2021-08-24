package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Policy {
    private int id;
    private String url;
    private String number;
    private int orderId;
}
