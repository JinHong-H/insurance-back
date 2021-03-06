package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.controller.entity.CreateInsuranceRequestBody;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import cn.wghtstudio.insurance.service.entity.GetOrderDetailResponseBody;
import cn.wghtstudio.insurance.service.entity.GetPolicyResponseBody;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
            @RequestParam(value = "endTime", required = false) String filterEndTime,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "licensePlate", required = false) String licensePlate,
            @RequestParam(defaultValue = "false", value = "accurate") Boolean accurate
    ) {
        Map<String, Object> params = new HashMap<>() {
            {
                put("limit", pageSize);
                put("current", current);
                put("id", id);
                put("offset", (current - 1) * pageSize);
                put("filterStartTime", filterStartTime);
                put("filterEndTime", filterEndTime);
            }
        };

        if (licensePlate != null) {
            params.put("licensePlate", accurate ? licensePlate : "%" + licensePlate + "%");
        }

        if (filterEndTime == null && filterStartTime == null) {
            LocalDateTime dateTime = LocalDateTime.now();
            dateTime = dateTime.withHour(0).withMinute(0).withSecond(0).minusMinutes(13 * 30);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String defaultTime = formatter.format(dateTime);
            params.put("defaultTime", defaultTime);
        }

        try {
            GetInsuranceListResponseBody body = insuranceService.getAllList(user, params);
            return Result.success(body);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @GetMapping("/detail")
    public Result<GetOrderDetailResponseBody> getInsuranceDetail(@CurrentUser User user, @RequestParam(value = "id") int id) {
        Map<String, Object> params = new HashMap<>() {
            {
                put("id", id);
            }
        };
        try {
            GetOrderDetailResponseBody body = insuranceService.getOrderDetail(user, params);
            return Result.success(body);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping
    public Result<?> createInsurance(@CurrentUser User user, @Valid @RequestBody CreateInsuranceRequestBody req) {
        // ????????????????????????????????????
        if (req.getIdCard() == null && req.getBusinessLicense() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }
        if (req.getDrivingLicense() == null && req.getCertificate() == null) {
            return Result.error(ResultEnum.ARGUMENT_ERROR);
        }

        // ???????????? ID ??????
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

    @DeleteMapping
    public Result<?> deleteInsurance(@CurrentUser User user, @RequestParam(value = "id") int id) {
        Map<String, Object> params = new HashMap<>() {
            {
                put("id", id);
            }
        };
        try {
            insuranceService.deleteOrder(user, params);
            return Result.success(null);
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

    @GetMapping("/policy")
    public Result<GetPolicyResponseBody> getPolicyList(
            @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize,
            @RequestParam(defaultValue = "1", value = "current") Integer current
    ) {
        try {
            Map<String, Object> params = new HashMap<>() {
                {
                    put("limit", pageSize);
                    put("current", current);
                    put("offset", (current - 1) * pageSize);
                }
            };

            GetPolicyResponseBody body = insuranceService.getPolicyList(params);
            return Result.success(body);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
