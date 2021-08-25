package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserListResponseBody {
    private int total;
    private List<GetUserListItem> items;
}
