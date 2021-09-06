package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserListItem {
    private int id;
    private String username;
    private String nickname;
    private int role;
}
