package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

// 车辆合格证
@Data
@Builder
public class CertificateResponseBody {
    private int id;
    private String engine;
    private String frame;
}
