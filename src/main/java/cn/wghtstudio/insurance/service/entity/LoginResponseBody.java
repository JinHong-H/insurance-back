package cn.wghtstudio.insurance.service.entity;

import lombok.Data;

@Data
public class LoginResponseBody {
    private String token;
    private int id;
    private String username;
}
