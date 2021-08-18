package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private int roleID;
    private Role role;
}
