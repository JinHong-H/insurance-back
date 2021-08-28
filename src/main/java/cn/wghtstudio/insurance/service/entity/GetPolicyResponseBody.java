package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPolicyResponseBody {
    private String number;
    private String processType;
}
