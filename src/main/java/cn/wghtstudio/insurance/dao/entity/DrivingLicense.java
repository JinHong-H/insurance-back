package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class DrivingLicense {
    private Integer id;
    private String url;
    private String owner;
    private String plateNumber;
    private String engine;
    private String frame;
    private String type;
    private Integer orderId;

    @Tolerate
    public DrivingLicense() {
    }
}
