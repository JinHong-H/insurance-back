package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.exception.JsonParseErrorException;
import cn.wghtstudio.insurance.exception.OcrTokenGetErrorException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
public class OcrController {
    @Resource
    private OcrInfoService ocrInfoService;

    @Getter
    @Setter
    static class OcrRequestBody {
        @NotEmpty
        private String imgUrl;
    }

    @PostMapping("/insurance")
    public Result<InsuranceDocumentResponseBody> GetInsuranceInfo(@Valid @RequestBody OcrController.OcrRequestBody req) {
        try {
            InsuranceDocumentResponseBody res = ocrInfoService.GetInsuranceDocumentService(req.imgUrl);
            return Result.success(res);
        } catch (JsonParseErrorException e) {
            return Result.error(ResultEnum.JSON_PARSE_ERROR);
        } catch (OcrTokenGetErrorException e) {
            return Result.error(ResultEnum.TOKEN_GET_ERROR);
        } catch (Exception e) {
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/idcard")
    public Result<IdCardResponseBody> GetIdCardInfo(@Valid @RequestBody OcrController.OcrRequestBody req) {
        try {
            IdCardResponseBody res = ocrInfoService.GetIdCardInfoService(req.imgUrl);
            return Result.success(res);
        } catch (JsonParseErrorException e) {
            return Result.error(ResultEnum.JSON_PARSE_ERROR);
        } catch (OcrTokenGetErrorException e) {
            return Result.error(ResultEnum.TOKEN_GET_ERROR);
        } catch (Exception e) {
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/bussylicense")
    public Result<BussyLicenseResponseBdoy> GetBussyLicenseInfo(@Valid @RequestBody OcrController.OcrRequestBody req) {
        try {
            BussyLicenseResponseBdoy res = ocrInfoService.GetBussyLicenseInfoService(req.imgUrl);
            return Result.success(res);
        } catch (JsonParseErrorException e) {
            return Result.error(ResultEnum.JSON_PARSE_ERROR);
        } catch (OcrTokenGetErrorException e) {
            return Result.error(ResultEnum.TOKEN_GET_ERROR);
        } catch (Exception e) {
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/drivelicense")
    public Result<DriveLicenseResponseBody> GetDriveLicenseInfo(@Valid @RequestBody OcrController.OcrRequestBody req) {
        try {
            DriveLicenseResponseBody res = ocrInfoService.GetDriveLicenseInfoService(req.imgUrl);
            return Result.success(res);
        } catch (JsonParseErrorException e) {
            return Result.error(ResultEnum.JSON_PARSE_ERROR);
        } catch (OcrTokenGetErrorException e) {
            return Result.error(ResultEnum.TOKEN_GET_ERROR);
        } catch (Exception e) {
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/certificate")
    public Result<CertificateResponseBody> GetCertificateInfo(@Valid @RequestBody OcrController.OcrRequestBody req) {
        try {
            CertificateResponseBody res = ocrInfoService.GetCertificateInfoService(req.imgUrl);
            return Result.success(res);
        } catch (JsonParseErrorException e) {
            return Result.error(ResultEnum.JSON_PARSE_ERROR);
        } catch (OcrTokenGetErrorException e) {
            return Result.error(ResultEnum.TOKEN_GET_ERROR);
        } catch (Exception e) {
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

}
