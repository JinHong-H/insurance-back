package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class Policy {
    private int id;
    private String name;
    private String url;
    private String number;
    private Integer processType;
    private Integer orderId;

    @Tolerate
    public Policy() {
    }
}
