package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

// 行驶证
@Data
@Builder
public class DrivingLicenseResponseBody {
    private int id;
    private String plate;
    private String engine;
    private String frame;
    private String type;
}
