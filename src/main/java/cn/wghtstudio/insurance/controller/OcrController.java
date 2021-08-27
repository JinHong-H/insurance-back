package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.controller.entity.OcrRequestBody;
import cn.wghtstudio.insurance.exception.OCRException;
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
    public Result<IdCardResponseBody> getInsuranceInfo(@Valid @RequestBody OcrRequestBody req) {
        try {
            IdCardResponseBody res = ocrInfoService.idCardInfoService(req.getImgUrl());
            return Result.success(res);
        } catch (OCRException e) {
            logger.warn("OCRException", e);
            return Result.error(ResultEnum.OCR_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/business")
    public Result<BusinessLicenseResponseBody> getBusinessInfo(@Valid @RequestBody OcrRequestBody req) {
        try {
            BusinessLicenseResponseBody res = ocrInfoService.businessInfoService(req.getImgUrl());
            return Result.success(res);
        } catch (OCRException e) {
            logger.warn("OCRException", e);
            return Result.error(ResultEnum.OCR_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/driving")
    public Result<DrivingLicenseResponseBody> getDrivingInfo(@Valid @RequestBody OcrRequestBody req) {
        try {
            DrivingLicenseResponseBody res = ocrInfoService.drivingInfoService(req.getImgUrl());
            return Result.success(res);
        } catch (OCRException e) {
            logger.warn("OCRException", e);
            return Result.error(ResultEnum.OCR_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/certificate")
    public Result<CertificateResponseBody> getCertificateInfo(@Valid @RequestBody OcrRequestBody req) {
        try {
            CertificateResponseBody res = ocrInfoService.certificateInfoService(req.getImgUrl());
            return Result.success(res);
        } catch (OCRException e) {
            logger.warn("OCRException", e);
            return Result.error(ResultEnum.OCR_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/otherFile")
    public Result<OtherFileResponseBody> saveOtherFile(@Valid @RequestBody OcrRequestBody req) {
        try {
            OtherFileResponseBody res = ocrInfoService.otherFileService(req.getImgUrl());
            return Result.success(res);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/policy")
    public Result<InsurancepolicyResponseBody> getPolicyInfo(@Valid @RequestBody OcrRequestBody req) {
        try {
            InsurancepolicyResponseBody res = ocrInfoService.insurance(req.getImgUrl());
            return Result.success(res);
        } catch (OCRException e) {
            logger.warn("OCRException", e);
            return Result.error(ResultEnum.OCR_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
