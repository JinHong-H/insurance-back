package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/insurance")
public class InsuranceController {
    private final Logger logger = LoggerFactory.getLogger(InsuranceController.class);

    @Resource
    InsuranceService insuranceService;

    @GetMapping
    public Result<GetInsuranceListResponseBody> getInsuranceList(
            @CurrentUser User user,
            @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize,
            @RequestParam(defaultValue = "0", value = "current") Integer current,
            @RequestParam(value = "startTime", required = false) String filterStartTime,
            @RequestParam(value = "endTime", required = false) String filterEndTime
    ) {
        try {
            Map<String, Object> params = new HashMap<>() {
                {
                    put("limit", pageSize);
                    put("current", current);
                    put("offset", (current - 1) * pageSize);
                    put("filterStartTime", filterStartTime);
                    put("filterEndTime", filterEndTime);
                }
            };

            GetInsuranceListResponseBody body = insuranceService.getAllList(user, params);
            return Result.success(body);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @GetMapping(path = "/export")
    public Result<String> exportExcel(@RequestParam(value = "id", required = false) List<Integer> ids) {
        return Result.success("/insurance/export");
    }
}
