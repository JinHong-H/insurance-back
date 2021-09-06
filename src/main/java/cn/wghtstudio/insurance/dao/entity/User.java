package cn.wghtstudio.insurance.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class User {
    private int id;
    private String username;
    private String nickname;
    private String password;
    private Integer roleID;
    private Role role;

    @Tolerate
    public User() {
    }
}
