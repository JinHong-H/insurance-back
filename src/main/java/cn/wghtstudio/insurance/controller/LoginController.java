package cn.wghtstudio.insurance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
  @ResponseBody
  @PostMapping("/login")
  public String Login() {
    return "login";
  }
}
