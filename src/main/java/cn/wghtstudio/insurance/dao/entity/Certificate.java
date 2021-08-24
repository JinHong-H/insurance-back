package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class Certificate {
    private Integer id;
    private String url;
    private String engine;
    private String frame;
    private Integer orderId;

    @Tolerate
    public Certificate() {
    }
}
