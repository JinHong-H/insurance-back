package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPolicyResponseBody {
    private int total;
    private int pageSize;
    private int current;
    List<GetPolicyListItem> items;
}
