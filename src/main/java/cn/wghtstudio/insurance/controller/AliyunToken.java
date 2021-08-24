package cn.wghtstudio.insurance.controller;


import cn.wghtstudio.insurance.exception.PasswordErrorException;
import cn.wghtstudio.insurance.exception.SignTokenException;
import cn.wghtstudio.insurance.exception.UserNotFoundException;
import cn.wghtstudio.insurance.service.AliyunService;
import cn.wghtstudio.insurance.service.entity.LoginResponseBody;
import cn.wghtstudio.insurance.service.entity.STSTokenResponseToken;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/aliyun")
public class AliyunToken {
    private final Logger logger = LoggerFactory.getLogger(AliyunToken.class);

    @Resource
    private AliyunService aliyunService;

    @GetMapping("/sts")
    public Result<STSTokenResponseToken> getSTSToken() {
        try {
            STSTokenResponseToken res = aliyunService.getSTSToken();
            return Result.success(res);
        } catch (ClientException e) {
            logger.warn("ClientException", e);
            return Result.error(ResultEnum.ALIYUN_AUTH_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
