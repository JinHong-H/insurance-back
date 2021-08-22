package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

// 身份证
@Data
@Builder
public class IdCardResponseBody {
    private int id;
    private String address;
    private String number;
    private String name;
}
