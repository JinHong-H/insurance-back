package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class Policy {
    private int id;
    private String url;
    private String number;
    private String processType;
    private Integer orderId;

    @Tolerate
    public Policy() {
    }
}
