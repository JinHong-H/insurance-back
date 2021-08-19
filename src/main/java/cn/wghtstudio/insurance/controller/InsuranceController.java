package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.exception.AuthNotMatchException;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class InsuranceController {
    private final Logger logger = LoggerFactory.getLogger(InsuranceController.class);

    @Resource
    InsuranceService insuranceService;

    @GetMapping(value = "/insurance")
    public Result<GetInsuranceListResponseBody> getInsuranceList(
            @CurrentUser User user
    ) {
        try {
            GetInsuranceListResponseBody body = insuranceService.getAllList(user);
            return Result.success(body);
        } catch (AuthNotMatchException e) {
            logger.warn("AuthNotMatchException", e);
            return Result.error(ResultEnum.AUTH_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
