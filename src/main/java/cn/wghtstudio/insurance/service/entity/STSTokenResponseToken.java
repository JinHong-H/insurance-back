package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class STSTokenResponseToken {
    private String accessKey;
    private String accessKeySecret;
    private String securityToken;
}
