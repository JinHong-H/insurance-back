package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.controller.entity.OcrRequestBody;
import cn.wghtstudio.insurance.exception.GetOcrTokenErrorException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ocr")
public class OcrController {
    private final Logger logger = LoggerFactory.getLogger(OcrController.class);

    @Resource
    private OcrInfoService ocrInfoService;

    @PostMapping("/idCard")
    public Result<?> GetInsuranceInfo(@Valid @RequestBody OcrRequestBody req) {
        try {
            IdCardResponseBody res = ocrInfoService.idCardInfoService(req.getImgUrl());
            return Result.success(res);
        } catch (GetOcrTokenErrorException e) {
            logger.warn("GetOcrTokenErrorException", e);
            return Result.error(ResultEnum.TOKEN_GET_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
