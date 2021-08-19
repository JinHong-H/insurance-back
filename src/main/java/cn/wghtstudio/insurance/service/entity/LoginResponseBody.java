package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseBody {
    private String token;
    private int id;
    private String username;
    private int role;
}
