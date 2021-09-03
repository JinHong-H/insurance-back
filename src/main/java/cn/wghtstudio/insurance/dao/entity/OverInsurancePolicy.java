package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


@Data
@Builder
public class OverInsurancePolicy {
    private int id;
    private String name;
    private String url;
    private Integer orderId;

    @Tolerate
    public OverInsurancePolicy() {
    }
}
