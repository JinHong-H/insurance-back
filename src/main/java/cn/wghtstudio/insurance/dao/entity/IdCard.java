package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class IdCard {
    private Integer id;
    private String url;
    private String name;
    private String number;
    private String address;
    private Integer orderId;

    @Tolerate
    public IdCard() {
    }
}
