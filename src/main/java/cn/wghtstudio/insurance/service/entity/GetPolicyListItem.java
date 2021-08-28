package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPolicyListItem {
    private int id;
    private String name;
    private String url;
    private String number;
    private int processType;
}
