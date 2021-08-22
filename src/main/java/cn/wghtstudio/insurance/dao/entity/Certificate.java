package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Certificate {
    private int id;
    private String url;
    private String engine;
    private String frame;
    private int orderId;
}
