package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsurancepolicyResponseBody {
    private String plateNumber;
    private String number;
    private String frame;
    private String engine;
}
