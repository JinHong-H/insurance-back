package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class GetInsuranceListResponseBody {
    private int total;
    private int pageSize;
    private int offset;
    @Singular
    private List<GetInsuranceListItem> items;
}
