package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsuranceController {
    @GetMapping(value = "/insurance")
    public Result<String> getInsuranceList(@CurrentUser User user) {
        System.out.println(user);
        return Result.success("insurance");
    }
}
