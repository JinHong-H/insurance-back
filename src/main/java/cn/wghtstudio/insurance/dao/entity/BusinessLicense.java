package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class BusinessLicense {
    private Integer id;
    private String url;
    private String name;
    private String address;
    private String number;
    private Integer orderId;

    @Tolerate
    public BusinessLicense() {
    }
}
