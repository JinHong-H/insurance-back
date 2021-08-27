package cn.wghtstudio.insurance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunConfig {
    private String accessKeyID;
    private String accessKeySecret;
    private String arn;
}
