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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InsuranceController {
    private final Logger logger = LoggerFactory.getLogger(InsuranceController.class);

    @Resource
    InsuranceService insuranceService;

    @GetMapping(value = "/insurance")
    public Result<GetInsuranceListResponseBody> getInsuranceList(
            @CurrentUser User user,
            @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize,
            @RequestParam(defaultValue = "0", value = "offset") Integer offset
    ) {
        try {
            Map<String, Object> params = new HashMap<>() {
                {
                    put("limit", pageSize);
                    put("offset", offset);
                }
            };

            GetInsuranceListResponseBody body = insuranceService.getAllList(user, params);
            return Result.success(body);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
