package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @PostMapping("/login")
    public Result<String> Login() {
        return Result.success("login");
    }
}
