package cn.wghtstudio.insurance.controller.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequestBody {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
