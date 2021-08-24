package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.controller.entity.CreateInsuranceRequestBody;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            @RequestParam(defaultValue = "1", value = "current") Integer current,
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

    @PostMapping
    public Result<?> createInsurance(@CurrentUser User user, @Valid @RequestBody CreateInsuranceRequestBody req) {
        // 证明材料必须存在其中一项
        if (req.getIdCard() == null && req.getBusinessLicense() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }
        if (req.getDrivingLicense() == null && req.getCertificate() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }

        // 必须传递 ID 字段
        if (req.getIdCard() != null && req.getIdCard().getId() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }
        if (req.getBusinessLicense() != null && req.getBusinessLicense().getId() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }
        if (req.getDrivingLicense() != null && req.getDrivingLicense().getId() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }
        if (req.getCertificate() != null && req.getCertificate().getId() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }

        try {
            insuranceService.createNewOrder(user, req);
            return Result.success(null);
        } catch (ParseException e) {
            logger.warn("ParseException", e);
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @GetMapping(path = "/export")
    public void exportExcel(
            HttpServletResponse response,
            @CurrentUser User user,
            @RequestParam(value = "id", required = false) List<Integer> ids,
            @RequestParam(value = "startTime", required = false) String filterStartTime,
            @RequestParam(value = "endTime", required = false) String filterEndTime
    ) {
        Map<String, Object> params = new HashMap<>() {
            {
                put("ids", ids);
                put("filterStartTime", filterStartTime);
                put("filterEndTime", filterEndTime);
            }
        };
        try {
            insuranceService.exportExcel(response, user, params);
        } catch (IOException e) {
            logger.warn("IOException", e);
        }
    }
}
