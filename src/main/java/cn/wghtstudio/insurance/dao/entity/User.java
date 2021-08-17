package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

@Data
public class User {
    private String username;
    private String password;
    private String role;
}
