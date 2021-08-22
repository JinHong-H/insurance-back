package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

// 营业执照
@Data
@Builder
public class BusinessLicenseResponseBody {
    private int id;
    private String address;
    private String number;
    private String name;
}
