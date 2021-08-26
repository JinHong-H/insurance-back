package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class OtherFile {
    private int id;
    private String url;
    private Integer orderId;

    @Tolerate
    public OtherFile() {
    }
}
